package com.hotel.services;

import com.hotel.dao.user.CustomerRepositoryInterface;
import com.hotel.interfaces.Count;
import com.hotel.models.Customer;

import java.util.List;

public class CustomerService  {
    CustomerRepositoryInterface customerRepository;
    Count count;
    public CustomerService(CustomerRepositoryInterface CustomerRepository , Count count) {
        this.customerRepository = CustomerRepository;
        this.count = count;
    }
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
    public boolean deleteCustomer(Customer customer) {
        return customerRepository.delete(customer);
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.all();
    }
    public int getCustomerCount() {
        return count.count();
    }
}
