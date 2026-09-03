package com.ridex.ridex.service;

import com.ridex.ridex.entity.Customer;
import com.ridex.ridex.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get customer by ID
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // Register new customer
    public Customer registerCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Update customer
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existing = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found!"));
        existing.setFullName(updatedCustomer.getFullName());
        existing.setPhone(updatedCustomer.getPhone());
        existing.setAddress(updatedCustomer.getAddress());
        return customerRepository.save(existing);
    }

    // Delete customer
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // Search customer by name
    public List<Customer> searchCustomers(String name) {
        return customerRepository.findByFullNameContainingIgnoreCase(name);
    }

    // Count active customers
    public long countActiveCustomers() {
        return customerRepository.countByStatus(Customer.CustomerStatus.ACTIVE);
    }
}
