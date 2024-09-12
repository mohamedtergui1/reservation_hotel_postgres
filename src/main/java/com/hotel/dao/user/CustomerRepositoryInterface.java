package com.hotel.dao.user;

import com.hotel.models.Customer;

import java.util.ArrayList;

public interface CustomerRepositoryInterface {
    public boolean insert(Customer customer);
    public boolean update(Customer customer);
    public boolean delete(Customer customer);
    public ArrayList<Customer> all();
    public Customer getById(Integer id);
}
