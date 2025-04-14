package com.example.demo.views.services;

import com.example.demo.views.repositories.CheckRepository;
import com.example.demo.views.repositories.database_entities.Check;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.UUID;

@Service
public class CheckService extends AbstractService<Check, UUID> {

    private final CheckRepository repository;

    public CheckService(PlatformTransactionManager transactionManager, CheckRepository repository) {
        super(transactionManager);
        this.repository = repository;
    }

    @Override
    public List<Check> getAllEntities() {
        return repository.findAll();
    }

    @Override
    public void addEntity(Check e) {
        repository.save(e);
    }

    @Override
    public void updateEntity(Check e) {
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
}
