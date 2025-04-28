package com.example.demo.services;

import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.database_entities.CustomerCard;
import com.vaadin.hilla.Nullable;
import jakarta.validation.ConstraintViolationException;
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
        transactionTemplate.execute(status -> {
            if (repository.existsPhoneNumber(e.getPhoneNumber())) {
                throw new ConstraintViolationException("Phone number already exists", null);
            }
            repository.save(e);
            return null;
        });
    }


    @Override
    public void updateEntity(CustomerCard e) {
        transactionTemplate.execute(status -> {
            if (repository.existsPhoneNumber(e.getPhoneNumber(), e.getId())) {
                throw new ConstraintViolationException("Phone number already exists", null);
            }
        repository.update(e);
        return null;
        });
    }

    @Override
    public void deleteEntity(UUID id) {
        transactionTemplate.execute(status -> {
        if (repository.existsChecksLinkedTo(id)) {
            throw new ConstraintViolationException("Cannot delete customer, since there are checks linked to him", null);
        }
        repository.delete(id);
            return null;
        });
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

    @Nullable
    public List<CustomerCard> getAllBy(String s, String phone, Integer percent) {
        return repository.getAllBy(s, phone, percent);
    }
}