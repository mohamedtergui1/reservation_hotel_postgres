package com.hotel.dao.user;

import com.hotel.database.Orm;
import com.hotel.models.Customer;
import com.hotel.models.Reservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CustomerRepository extends Orm<Customer> implements CustomerRepositoryInterface {
    public Class<Customer> getEntityClass() {
        return Customer.class;
    }
    @Override
    protected Set<Class<?>> manyToOneRelations() {
        return new HashSet<>(Arrays.asList(Reservation.class));
    }
    public boolean insert(Customer customer){return super.insert(customer);}
    public boolean update(Customer customer){return super.update(customer);}
    public boolean delete(Customer customer){return super.delete(customer);}
    public ArrayList<Customer> all(){return super.all();}
    public Customer getById(Integer id){return( (Customer) super.getById(id));}
}
