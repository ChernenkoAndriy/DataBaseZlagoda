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
        transactionTemplate.execute(status -> {
            checkRepository.save(check);
            check.setCheck_number(checkRepository.getIdBy(check));
            saveCheck(check.getGoods(), check.getCheck_number());
            return null;
        });
    }

    @Override
    public void updateEntity(Check check) {
        transactionTemplate.execute(status -> {
        checkRepository.update(check);
        saveCheck(check.getGoods(), check.getCheck_number());
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
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        return checkRepository.findFilteredChecks(employeeSurname, employeePhone, customerSurname, customerPhone, dateFrom, dateTo);
    }

    public void saveCheck(List<CheckEntry> goods , UUID checkId){
        for(int i = 0; i<goods.size(); i++){
            goods.get(i).setCheck_number(checkId);
        }
        transactionTemplate.execute(status -> {
            checkEntryRepository.deleteSales(goods.getFirst().getCheck_number());
            for (CheckEntry checkEntry : goods) {
                if (checkEntry.getDelta() != 0) {
                    checkRepository.subtractFromWareHouse(checkEntry);
                }
                checkEntry.setSelling_price(checkEntry.getSelling_price().add(checkEntry.getDeltaPrice()));
                checkEntry.setAmountOfProducts(checkEntry.getAmountOfProducts() + checkEntry.getDelta());
                checkEntryRepository.save(checkEntry);
            }
            return null;
        });
    }


}
