package com.hotel.database;

import com.hotel.interfaces.GetId;


import java.sql.*;
import java.lang.reflect.Field;
import java.util.*;


public abstract class Orm<T> {
    private final Connection con;

    protected Orm() {
        this.con = PostgreSQLDatabase.getInstance("jdbc:postgresql://localhost:5432/mydatabase", "myuser", "mypassword").getConnection();
    }
    protected abstract Class<T> getEntityClass();
    protected Set<Class<?>> manyRelations() {
        return new HashSet<>();
    }
    private static final Set<String> ALLOWED_TYPES = new HashSet<>(Arrays.asList("int","boolean" , "float", "java.lang.String", "char", "long", "double", "java.sql.Date"));

    public boolean insert(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        String tableName = getEntityClass().getSimpleName().toLowerCase();
        String sql = generateInsertSQL(tableName, obj);
        System.out.println(sql);

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            int parameterIndex = 1;
            for (Field field : getEntityClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value != null && !field.getName().equals("id")) {
                    if (ALLOWED_TYPES.contains(field.getType().getName())) {
                        if (value instanceof java.sql.Date) {
                            pstmt.setDate(parameterIndex++, (java.sql.Date) value);
                        } else {
                            pstmt.setObject(parameterIndex++, value);
                        }
                    } else if (value instanceof GetId) {
                        pstmt.setInt(parameterIndex++, ((GetId) value).getId());
                    } else {
                        throw new SQLException("Unsupported type: " + field.getType().getName());
                    }
                }
            }
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IllegalAccessException e) {
            System.out.println("Error during insert: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        String tableName = getEntityClass().getSimpleName().toLowerCase();
        String sql = generateDeleteSQL(tableName, obj);
        System.out.println(sql);

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            Field[] fields = getEntityClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals("id")) {
                    Object idValue = field.get(obj);
                    if (idValue != null) {
                        pstmt.setObject(1, idValue);
                    } else {
                        throw new IllegalArgumentException("ID value cannot be null for deletion.");
                    }
                    break;
                }
            }
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IllegalAccessException e) {
            System.out.println("Error during delete: " + e.getMessage());
            return false;
        }
    }

    public boolean update(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        String tableName = getEntityClass().getSimpleName().toLowerCase();
        String sql = generateUpdateSQL(tableName, obj);
        System.out.println("Generated SQL for update: " + sql);

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            int parameterIndex = 1;
            Field[] fields = getEntityClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value != null) {
                    String typeName = field.getType().getName();
                    if (ALLOWED_TYPES.contains(typeName)) {
                        if (value instanceof java.sql.Date) {
                            pstmt.setDate(parameterIndex++, (java.sql.Date) value);
                        } else if (!field.getName().equals("id")) {
                            pstmt.setObject(parameterIndex++, value);
                        }
                    } else if (value instanceof GetId) {
                        pstmt.setInt(parameterIndex++, ((GetId) value).getId());
                    } else {
                        throw new SQLException("Unsupported type: " + typeName);
                    }
                }
            }


            Field idField = getEntityClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(obj);
            if (idValue == null) {
                throw new IllegalArgumentException("Object must have a non-null 'id' field for update.");
            }
            pstmt.setObject(parameterIndex, idValue);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            System.out.println("Error during update: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<T> all() {
        return all(getEntityClass(), null, null);
    }

    public ArrayList<T> all(Class<T> entityClass, String columnName, Object value) {
        String tableName = entityClass.getSimpleName().toLowerCase();
        String sql = generateSelectQuery(tableName, columnName);

        ArrayList<T> results = new ArrayList<>();

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            if (value != null) {
                pstmt.setObject(1, value);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    T entity = entityClass.getDeclaredConstructor().newInstance();

                    for (Field field : entityClass.getDeclaredFields()) {
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        String fieldType = field.getType().getName();

                        String fieldColumnName = ALLOWED_TYPES.contains(fieldType) ? fieldName : fieldName.toLowerCase() + "_id";

                        Object columnValue = rs.getObject(fieldColumnName);

                        try {
                            switch (fieldType) {
                                case "java.lang.String":
                                    field.set(entity, columnValue != null ? columnValue.toString() : null);
                                    break;
                                case "int":
                                case "java.lang.Integer":
                                    field.set(entity, columnValue != null ? ((Number) columnValue).intValue() : null);
                                    break;
                                case "double":
                                case "java.lang.Double":
                                    field.set(entity, columnValue != null ? ((Number) columnValue).doubleValue() : null);
                                    break;
                                case "float":
                                case "java.lang.Float":
                                    field.set(entity, columnValue != null ? ((Number) columnValue).floatValue() : null);
                                    break;
                                case "long":
                                case "java.lang.Long":
                                    field.set(entity, columnValue != null ? ((Number) columnValue).longValue() : null);
                                    break;
                                case "boolean":
                                case "java.lang.Boolean":
                                    field.set(entity, columnValue != null ? ((Boolean) columnValue) : null);
                                    break;
                                case "java.sql.Date":
                                    field.set(entity, columnValue != null ? new java.sql.Date(((java.sql.Date) columnValue).getTime()) : null);
                                    break;
                                default:
                                    if (columnValue != null) {
                                        if (GetId.class.isAssignableFrom(field.getType())) {
                                            Object relatedEntity = getById((Integer) columnValue, field.getType().getName());
                                            field.set(entity, relatedEntity);
                                        } else {
                                            System.err.println("Unsupported field type: " + fieldType);
                                        }
                                    }
                                    break;
                            }
                        } catch (Exception e) {
                            System.err.println("Error setting field value: " + fieldName + " - " + e.getMessage());
                        }
                    }

                    results.add(entity);
                }
            }
        } catch (SQLException | ReflectiveOperationException e) {
            System.err.println("Error during query execution: " + e.getMessage());
        }

        return results;
    }

    public    Object getById(Integer id, String className) {
        if (id == null || className == null || className.isEmpty()) {
             System.err.println("Invalid id or className");
        }

        try {
            // Convert the class name to a Class object
            Class<?> clazz = Class.forName(className);
            String tableName = clazz.getSimpleName().toLowerCase(); // Use class name as table name
            String sql = generateSelectQuery(tableName, "id");


            // Prepare the statement
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                // Execute the query
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    // Check if a result is returned
                    if (resultSet.next()) {


                        Object instance = clazz.getDeclaredConstructor().newInstance();

                        // Get all fields of the class
                        Field[] fields = clazz.getDeclaredFields();

                        // Populate the instance with data from the result set
                        for (Field field : fields) {
                            field.setAccessible(true); // Allow access to private fields

                            String columnName = field.getName().toLowerCase() +
                                    (ALLOWED_TYPES.contains(field.getType().getName()) ? "" : "_id");

                            Object value = resultSet.getObject(columnName);

                            // Handle data type conversion based on field type
                            String fieldType = field.getType().getName();

                            switch (fieldType) {
                                case "java.lang.String":
                                    field.set(instance, value != null ? value.toString() : null);
                                    break;
                                case "int":
                                case "java.lang.Integer":
                                    field.set(instance, value != null ? ((Number) value).intValue() : null);
                                    break;
                                case "double":
                                case "java.lang.Double":
                                    field.set(instance, value != null ? ((Number) value).doubleValue() : null);
                                    break;
                                case "float":
                                case "java.lang.Float":
                                    field.set(instance, value != null ? ((Number) value).floatValue() : null);
                                    break;
                                case "long":
                                case "java.lang.Long":
                                    field.set(instance, value != null ? ((Number) value).longValue() : null);
                                    break;
                                case "boolean":
                                case "java.lang.Boolean":
                                    field.set(instance, value != null ? ((Boolean) value) : null);
                                    break;
                                case "java.sql.Date":
                                    field.set(instance, value != null ? new java.sql.Date(((java.sql.Date) value).getTime()) : null);
                                    break;
                                default:
                                    // Handle complex types or foreign keys
                                    if (value != null) {
                                        Class<?> fieldTypeClass = field.getType();
                                        Object relatedInstance = getById((Integer) value, fieldTypeClass.getName());
                                        field.set(instance, relatedInstance);
                                    }
                                    else
                                        field.set(instance, null);
                                    break;
                            }
                        }

                        return instance;
                    } else {
                        return null; // No record found
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during getById: " + className + " \n" + e.getMessage());
            return null; // Handle the exception as needed
        }
    }

    public  Object getById(Integer id){
        return getById(id,getEntityClass().getName());
    }

    public Map<String, ArrayList<?>> loadRelations(Object object) {
        Map<String, ArrayList<?>> loadRelations = new HashMap<>();

        for (Class<?> relation : manyRelations()) {

            if (object instanceof GetId ) {
                Object id = ((GetId) object).getId();

                loadRelations.put(relation.getSimpleName().toLowerCase() + "s",all( (Class<T>) relation,
                        getEntityClass().getSimpleName().toLowerCase() + "_id", id));
            }
        }
        return loadRelations;
    }

    public  int count()  {return count(getEntityClass(),null);}

    public static String generateCountQuery(Class<?> clazz, HashMap<String, HashMap<String, ArrayList<String>>> conditions) {
        String tableName = clazz.getSimpleName(); // Assuming the table name is the same as the class name

        // Start constructing the SQL query
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM ");
        queryBuilder.append(tableName);

        if ( conditions != null && !conditions.isEmpty()) {
            Iterator<Map.Entry<String, HashMap<String, ArrayList<String>>>> outerIterator = conditions.entrySet().iterator();

            while (outerIterator.hasNext()) {
                Map.Entry<String, HashMap<String, ArrayList<String>>> outerEntry = outerIterator.next();
                String operator = outerEntry.getKey(); // e.g., "AND" or "OR"
                HashMap<String, ArrayList<String>> columnsMap = outerEntry.getValue();

                queryBuilder.append(" WHERE ");

                Iterator<Map.Entry<String, ArrayList<String>>> innerIterator = columnsMap.entrySet().iterator();
                while (innerIterator.hasNext()) {
                    Map.Entry<String, ArrayList<String>> innerEntry = innerIterator.next();
                    String column = innerEntry.getKey();
                    ArrayList<String> values = innerEntry.getValue();

                    queryBuilder.append(column).append(" IN (");
                    for (int i = 0; i < values.size(); i++) {
                        if (i > 0) {
                            queryBuilder.append(", ");
                        }
                        queryBuilder.append("?");
                    }
                    queryBuilder.append(")");

                    if (innerIterator.hasNext()) {
                        queryBuilder.append(" ").append(operator).append(" ");
                    }
                }

                if (outerIterator.hasNext()) {
                    queryBuilder.append(" ");
                }
            }
        }
        System.out.println(queryBuilder.toString());
        return queryBuilder.toString();
    }

    public  int count(Class<?> clazz, HashMap<String, HashMap<String, ArrayList<String>>> conditions) {
        String query = generateCountQuery(clazz, conditions);

        try (
             PreparedStatement preparedStatement = this.con.prepareStatement(query)) {

            int paramIndex = 1;
            if ( conditions != null && !conditions.isEmpty()) {
                for (HashMap<String, ArrayList<String>> columnConditions : conditions.values()) {
                    for (ArrayList<String> values : columnConditions.values()) {
                        for (String value : values) {
                            preparedStatement.setObject(paramIndex++, value);
                        }
                    }
                }
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1); // Return the count
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    private String generateSelectQuery(String tableName, String columnName) {
        if (columnName != null && !columnName.isEmpty()) {
            // Generate SQL with WHERE condition
            return "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        } else {
            // Generate SQL without WHERE condition
            return "SELECT * FROM " + tableName;
        }
    }
    private String generateInsertSQL(String tableName, T obj) {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder values = new StringBuilder(" VALUES (");

        Field[] fields = getEntityClass().getDeclaredFields();
        boolean firstField = true;

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value != null && !field.getName().equals("id")) { // Skip the ID field
                    if (!firstField) {
                        sql.append(", ");
                        values.append(", ");
                    }

                    if (ALLOWED_TYPES.contains(field.getType().getName())) {
                        sql.append(field.getName());
                    } else {
                        sql.append(field.getName()).append("_id");
                    }
                    values.append("?");
                    firstField = false;
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error accessing field: " + e.getMessage());
            }
        }

        sql.append(")");
        values.append(")");
        sql.append(values);

        return sql.toString();
    }
    private String generateDeleteSQL(String tableName, T obj) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(tableName).append(" WHERE ");

        Field[] fields = getEntityClass().getDeclaredFields();
        boolean idFieldFound = false;

        for (Field field : fields) {
            if (field.getName().equals("id")) {
                sql.append(field.getName()).append(" = ?");
                idFieldFound = true;
                break;
            }
        }

        if (!idFieldFound) {
            throw new IllegalArgumentException("No ID field found in entity class.");
        }

        return sql.toString();
    }
    private String generateUpdateSQL(String tableName, T obj) {
        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET  ");
        Field[] fields = getEntityClass().getDeclaredFields();
        boolean firstField = true;

        for (Field field : fields) {

                field.setAccessible(true);


                if ( !field.getName().equals("id")) {
                    if (!firstField) {
                        sql.append(", ");
                    }
                    if (ALLOWED_TYPES.contains(field.getType().getName()))
                        sql.append(field.getName()).append(" = ?");
                    else
                        sql.append(field.getName().toLowerCase()).append("_id").append(" = ?");
                    firstField = false;
                }

        }

        sql.append(" WHERE id = ?");

        return sql.toString();
    }
}