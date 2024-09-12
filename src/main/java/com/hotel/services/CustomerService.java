package com.hotel.services;

import com.hotel.dao.user.CustomerRepositoryInterface;
import com.hotel.models.Customer;

public class CustomerService  {
    CustomerRepositoryInterface customerRepository;
    public CustomerService(CustomerRepositoryInterface CustomerRepository) {
        this.customerRepository = CustomerRepository;
    }
    public boolean addCustomer(Customer customer) {
        return customerRepository.insert(customer);
    }
    public Customer getCustomer(int id) {
        return customerRepository.getById(id);
    }
    public boolean updateCustomer(Customer customer) {
        return customerRepository.update(customer);
    }
}
