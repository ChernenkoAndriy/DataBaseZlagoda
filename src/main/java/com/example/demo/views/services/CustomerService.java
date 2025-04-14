package com.example.demo.views.services;

import com.example.demo.views.repositories.database_entities.CustomerCard;
import com.example.demo.views.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public List<CustomerCard> getAll() {
        return customerRepository.findAll();
    }

    public void save(CustomerCard customerCard) {
        customerRepository.save(customerCard);
    }

    public void delete(CustomerCard customerCard) {
        customerRepository.delete(customerCard);
    }

    public Collection<CustomerCard> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<CustomerCard> getFilteredCustomers(String surname, Integer discount) {
        if (surname != null && discount != null) {
            return customerRepository.findByCustSurnameContainingIgnoreCaseAndPercent(surname, discount);
        } else if (surname != null) {
            return customerRepository.findByCustSurnameContainingIgnoreCase(surname);
        } else if (discount != null) {
            return customerRepository.findByPercent(discount);
        } else {
            return customerRepository.findAll();
        }
    }

    public void saveCustomer(CustomerCard customer) {
        customerRepository.save(customer);
    }

    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }

}
