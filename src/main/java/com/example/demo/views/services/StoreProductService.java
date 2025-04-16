package com.example.demo.views.services;

import com.example.demo.views.repositories.StoreProductRepository;
import com.example.demo.views.repositories.database_entities.Store_Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.UUID;
@Service
public class StoreProductService extends AbstractService<Store_Product, UUID>{
    private StoreProductRepository repository;
    public StoreProductService(PlatformTransactionManager transactionManager, StoreProductRepository storeProductRepository) {
        super(transactionManager);
        repository = storeProductRepository;
    }

    @Override
    public List<Store_Product> getAllEntities() {
        return repository.findAll();
    }

    @Override
    public void addEntity(Store_Product e) {
        repository.save(e);
    }

    @Override
    public void updateEntity(Store_Product e) {
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
    protected void changeQuantity(UUID id, int delta) {
        repository.changeQuantity(id, delta);
    }
}
