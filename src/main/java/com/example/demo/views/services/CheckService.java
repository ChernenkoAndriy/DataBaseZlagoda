package com.example.demo.views.services;

import com.example.demo.views.repositories.CheckEntryRepository;
import com.example.demo.views.repositories.CheckRepository;
import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.repositories.database_entities.CheckEntry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CheckService extends AbstractService<Check, UUID> {
    private CheckRepository checkRepository;
    private CheckEntryRepository checkEntryRepository;

    public CheckService(PlatformTransactionManager transactionManager,
                        CheckRepository checkRepository,
                        CheckEntryRepository checkEntryRepository) {
        super(transactionManager);
        this.checkRepository = checkRepository;
        this.checkEntryRepository = checkEntryRepository;
    }

    @Override
    public List<Check> getAllEntities() {
        return checkRepository.findAll();
    }

    @Override
    public void addEntity(Check check) {
                    checkRepository.save(check);
    }

    @Override
    public void updateEntity(Check check) {
        checkRepository.update(check);
    }

    @Override
    public void deleteEntity(UUID id) {
        checkRepository.delete(id);
    }

    @Override
    public int countEntities() {
        return checkRepository.count();
    }

    public void setGoodsForCheck(Check e) {
        checkRepository.setGoodsFor(e);
    }

    public List<Check> findFilteredChecks(
            String employeeSurname,
            String employeePhone,
            String customerSurname,
            String customerPhone,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        return checkRepository.findFilteredChecks(employeeSurname, employeePhone, customerSurname, customerPhone, dateFrom, dateTo);
    }

    public int deleteCheckEntryWithReturn(UUID check, UUID storeProduct) {
        return transactionTemplate.execute(status -> {
            int returned = checkEntryRepository.returnGoods(check, storeProduct);
            int deleted = checkEntryRepository.deleteSale(check, storeProduct);
            return returned + deleted;
        });
    }

    public void addCheckEntry(CheckEntry checkEntry) {
        transactionTemplate.execute(e -> {
            boolean productAvailable = checkEntryRepository.isProductAvailable(checkEntry.getStore_product(), checkEntry.getAmountOfProducts());
            if (productAvailable) {
                checkEntryRepository.save(checkEntry);
            } else {
                throw new IllegalArgumentException("Not enough product available for sale.");
            }
            return e;
        });
    }


}
