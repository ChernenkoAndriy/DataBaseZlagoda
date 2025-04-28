package com.example.demo.views.services;

import com.example.demo.views.repositories.CategoryRepository;
import com.example.demo.views.repositories.database_entities.Category;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
@Service
public class CategoryService extends AbstractService<Category, Integer>{
    private CategoryRepository repository;
    public CategoryService(PlatformTransactionManager transactionManager, CategoryRepository repository) {
        super(transactionManager);
        this.repository = repository;
    }

    @Override
    public List<Category> getAllEntities() {
        return repository.findAll();
    }

    @Override
    public void addEntity(Category e) {
        try {
            repository.save(e);
        }catch (DuplicateKeyException ex){
            throw new ConstraintViolationException("Category with such name already exists", null);
        }
    }
    @Override
    public void updateEntity(Category e) {
        try {
        repository.update(e);
        }catch (DuplicateKeyException ex){
            throw new ConstraintViolationException("Category with such name already exists", null);
        }
    }

    @Override
    public void deleteEntity(Integer id) {
        try {
            repository.delete(id);
        }catch (DataIntegrityViolationException e){
            throw new ConstraintViolationException("Cannot delete not empty category", null);
        }

    }

    @Override
    public int countEntities() {
        return repository.count();
    }

    public List<Category> getAllBy(String name){
        return repository.getAllBy(name);
    }
    public List<Category> getAllWithoutSales(){
        return repository.getAllWithoutSales();
    }
}
