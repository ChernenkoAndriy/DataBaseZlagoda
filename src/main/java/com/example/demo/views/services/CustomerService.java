package com.example.demo.views.services;

import com.example.demo.views.repositories.CustomerRepository;
import com.example.demo.views.repositories.database_entities.CustomerCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.UUID;
@Service
public class CustomerService extends AbstractService<CustomerCard, UUID>{
    private CustomerRepository repository;
    public CustomerService(PlatformTransactionManager transactionManager, CustomerRepository repository) {
        super(transactionManager);
        this.repository=repository;
    }

    @Override
    public List<CustomerCard> getAllEntities() {
        return repository.findAll();
    }

    @Override
    public void addEntity(CustomerCard e) {
        repository.save(e);
    }

    @Override
    public void updateEntity(CustomerCard e) {
        repository.update(e);
    }

    @Override
    public void deleteEntity(UUID id) {
        repository.delete(id);
    }

    @Override
    public int countEntities() {
        return repository.count();
    }
    public List<String> customersPhones(){
        return repository.customersPhones();
    }
    public CustomerCard getCustomer(String phoneNumber){
        return repository.getCustomer(phoneNumber);
    }
}