package com.example.demo.views.services;

import com.example.demo.views.repositories.CheckEntryRepository;
import com.example.demo.views.repositories.CheckRepository;
import com.example.demo.views.repositories.database_entities.Check;
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
    public void addEntity(Check e) {
        checkRepository.save(e);
    }

    @Override
    public void updateEntity(Check e) {
        checkRepository.update(e);
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
        e.setGoods(checkEntryRepository.findById(e.getId()));
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
}
