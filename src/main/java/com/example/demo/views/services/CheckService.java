package com.example.demo.views.services;

import com.example.demo.views.repositories.CheckEntryRepository;
import com.example.demo.views.repositories.CheckRepository;
import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.repositories.database_entities.CheckEntry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        transactionTemplate.execute(status -> {
            List<CheckEntry> checkEntries = check.getGoods();
            if (!checkEntries.isEmpty()) {
                for (CheckEntry c : checkEntries) {
                    int maxCount = checkEntryRepository.getMaxCount(c);
                    if (maxCount < c.getAmountOfProducts()) {
                        throw new IllegalArgumentException("There are only " + maxCount + " items in warehouse for product: " + c.getProductName());
                    }
                }

                UUID checkNumber = checkRepository.saveAndReturnCheckNumber(check);
                check.setCheck_number(checkNumber);

                for (CheckEntry c : checkEntries) {
                    c.setCheck_number(checkNumber);
                    checkEntryRepository.save(c);
                }
            }

            return null;
        });
    }

    @Override
    public void updateEntity(Check check) {
        transactionTemplate.execute(status -> {
            List<CheckEntry> checkEntries = check.getGoods();
            if (!checkEntries.isEmpty()) {
                for (CheckEntry c : checkEntries) {
                    int maxCount = checkEntryRepository.getMaxCount(c);
                    if (maxCount < c.getAmountOfProducts()) {
                        throw new IllegalArgumentException("There are only " + maxCount + " items in warehouse for product: " + c.getProductName());
                    }
                }
            }

            checkRepository.update(check);

            saveCheck(check.getGoods(), check.getCheck_number());

            return null;
        });
    }

    public void saveCheck(List<CheckEntry> goods, UUID checkId) {
        transactionTemplate.execute(status -> {
            // 1. Перевірка ВСІХ товарів перед додаванням
            for (CheckEntry c : goods) {
                int maxCount = checkEntryRepository.getMaxCount(c);
                if (maxCount < c.getAmountOfProducts()) {
                    throw new IllegalArgumentException("There are only " + maxCount + " items in warehouse for product: " + c.getProductName());
                }
            }

            // 2. Видалити старі записи
            List<CheckEntry> oldGoods = checkEntryRepository.findById(checkId);
            for (CheckEntry c : oldGoods) {
                deleteSalesWithReturn(c);
            }

            // 3. Додати нові рядки чека
            for (CheckEntry c : goods) {
                c.setCheck_number(checkId); // важливо: призначити check_number!
                checkEntryRepository.save(c);
            }

            return null;
        });
    }

    public void deleteSalesWithReturn(CheckEntry c){
        int delta = c.getAmountOfProducts();
        transactionTemplate.execute(status -> {
            checkEntryRepository.delete(c);
            checkEntryRepository.returnGoods(delta, c.getStore_product());
                       return null;
        });
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
            LocalDateTime dateFrom,
            LocalDateTime dateTo
    ) {
        return checkRepository.findFilteredChecks(employeeSurname, employeePhone, customerSurname, customerPhone, dateFrom, dateTo);
    }
}
