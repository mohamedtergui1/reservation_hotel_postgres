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

    private static final Set<String> ALLOWED_TYPES = new HashSet<>(Arrays.asList("int", "float", "java.lang.String", "char", "long", "double", "java.sql.Date"));

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

    public synchronized ArrayList<T> all() {
        String tableName = getEntityClass().getSimpleName().toLowerCase();
        String sql = generateSelectQuery(tableName, getEntityClass());
        System.out.println("Generated SQL: " + sql);

        ArrayList<T> results = new ArrayList<>();

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                T entity = getEntityClass().getDeclaredConstructor().newInstance();

                for (Field field : getEntityClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    String fieldType = field.getType().getName();

                    // Determine column name based on field type
                    String columnName = ALLOWED_TYPES.contains(fieldType) ? fieldName : fieldName.toLowerCase() + "_id";

                    // Retrieve the value from ResultSet
                    Object value = rs.getObject(columnName);

                    // Validate and set the value based on field type
                    try {
                        switch (fieldType) {
                            case "java.lang.String":
                                field.set(entity, value != null ? value.toString() : null);
                                break;
                            case "int":
                            case "java.lang.Integer":
                                field.set(entity, value != null ? ((Number) value).intValue() : null);
                                break;
                            case "double":
                            case "java.lang.Double":
                                field.set(entity, value != null ? ((Number) value).doubleValue() : null);
                                break;
                            case "float":
                            case "java.lang.Float":
                                field.set(entity, value != null ? ((Number) value).floatValue() : null);
                                break;
                            case "long":
                            case "java.lang.Long":
                                field.set(entity, value != null ? ((Number) value).longValue() : null);
                                break;
                            case "boolean":
                            case "java.lang.Boolean":
                                field.set(entity, value != null ? ((Boolean) value) : null);
                                break;
                            case "java.sql.Date":
                                field.set(entity, value != null ? new java.sql.Date(((java.sql.Date) value).getTime()) : null);
                                break;
                            default:

                                if (value != null) {
                                    // Ensure that the field type is a class type
                                    if (GetId.class.isAssignableFrom(field.getType())) {

                                        Object relatedEntity = getById((Integer) value, field.getType().getName());
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
        } catch (SQLException | ReflectiveOperationException e) {
            // Replace with a logging framework in a real application
            System.err.println("Error during query execution: " + e.getMessage());
        }

        return results;
    }

    public synchronized  Object getById(Integer id, String className) {
        if (id == null || className == null || className.isEmpty()) {
             System.err.println("Invalid id or className");
        }

        try {
            // Convert the class name to a Class object
            Class<?> clazz = Class.forName(className);
            String tableName = clazz.getSimpleName().toLowerCase(); // Use class name as table name
            String sql = generateSelectQuery(tableName, clazz) + " WHERE id = ?";
            System.out.println("Generated SQL: " + sql);

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



 //generator functions for sql query <select ,insert ,delete ,update>
    private String generateSelectQuery(String table, Class<?> clazz) {
        StringBuilder sql = new StringBuilder("SELECT ");
        Field[] fields = clazz.getDeclaredFields();
        boolean first = true;

        for (Field field : fields) {
            if (!first) {
                sql.append(", ");
            }

            String fieldName = field.getName();
            String fieldType = field.getType().getName();

            // Use the field name if its type is allowed; otherwise, modify the name
            if (ALLOWED_TYPES.contains(fieldType)) {
                sql.append(fieldName);
            } else {
                sql.append(fieldName.toLowerCase());
                sql.append("_id");
            }

            first = false;
        }

        sql.append(" FROM ").append(table);
        return sql.toString();
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