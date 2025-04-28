package com.example.demo.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.List;
public abstract class AbstractService<T, TPK> {
    protected final TransactionTemplate transactionTemplate;

    @Autowired
    public AbstractService(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public abstract List<T> getAllEntities();
    public abstract void addEntity(T e);
    public abstract void updateEntity(T e);
    public abstract void deleteEntity(TPK id);
    public abstract int countEntities();
}
