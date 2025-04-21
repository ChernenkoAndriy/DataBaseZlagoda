package com.example.demo.views.services;

import com.example.demo.views.repositories.ProductRepository;
import com.example.demo.views.repositories.database_entities.Product;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Service
public class ProductService extends AbstractService<Product, Integer>{
    private ProductRepository repository;
    public ProductService(PlatformTransactionManager transactionManager, ProductRepository repository) {
        super(transactionManager);
        this.repository = repository;
    }

    @Override
    public List<Product> getAllEntities() {
        return repository.findAll();
    }

    @Override
    public void addEntity(Product e) {
        try {
            repository.save(e);
        }catch (DuplicateKeyException ex){
            throw  new ConstraintViolationException("Product with such name already exists", null);
        }
    }

    @Override
    public void updateEntity(Product e) {
        try {
            repository.update(e);
        }catch (DuplicateKeyException ex){
            throw  new ConstraintViolationException("Product with such name already exists", null);
        }
    }

    @Override
    public void deleteEntity(Integer id) {
        try {
            repository.delete(id);
        }catch (DataIntegrityViolationException e){
            throw new ConstraintViolationException("Cannot delete the product, cause there goods linked to him", null);
        }
    }

    @Override
    public int countEntities() {
        return repository.count();
    }

    public List<Product> getAllBy(String name, Integer categoryNumber) {
        return repository.getAllBy(name, categoryNumber);
    }
}
