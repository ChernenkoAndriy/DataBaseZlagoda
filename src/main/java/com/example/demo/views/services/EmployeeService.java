package com.example.demo.views.services;

import com.example.demo.views.repositories.database_entities.Employee;
import com.example.demo.views.repositories.EmployeeRepository;
import com.vaadin.hilla.Nullable;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class EmployeeService extends AbstractService<Employee, UUID>{

    private EmployeeRepository employeeRepository;
    private AuthorizationService authorizationService;

    @Autowired
    public EmployeeService(PlatformTransactionManager transactionManager, EmployeeRepository employeeRepository, AuthorizationService authorizationService) {
        super(transactionManager);
        this.employeeRepository=employeeRepository;
        this.authorizationService = authorizationService;
    }

    public List<Employee> getAllEntities() {
        return employeeRepository.findAll();
    }

    public void addEntity(Employee employee) {
        if (employee.getDate_of_birth().isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("Employee must be at least 18 years old");
        }
        if (employee.getDate_of_start().isBefore(employee.getDate_of_birth().plusYears(18))) {
            throw new IllegalArgumentException("Employee must be at least 18 at the start date");
        }
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (employeeRepository.existsByPhoneNumber(employee.getPhone_number())) {
                    throw new ConstraintViolationException("Phone number already exists!", null);
                }
                employeeRepository.save(employee);
            }
        });
    }

    public void addEntity(Employee employee, String login, String password){
        transactionTemplate.execute(status -> {
            addEntity(employee);
            UUID id = employeeRepository.getIdByPhone(employee.getPhone_number());
            if(!authorizationService.check(login)){
                authorizationService.addUser(login, password, id);
            }else{
                throw new DuplicateKeyException("Such login already exists");
            }
            return null;
        });
    }

    public void updateEntity(Employee employee) {
        if (employee.getDate_of_birth().isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("Employee must be at least 18 years old");
        }
        if (employee.getDate_of_start().isBefore(employee.getDate_of_birth().plusYears(18))) {
            throw new IllegalArgumentException("Employee must be at least 18 at the start date");
        }

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (employeeRepository.existsByPhoneNumber(employee.getPhone_number(), employee.getId())) {
                    throw new ConstraintViolationException("Phone number already exists!", null);
                }
                employeeRepository.update(employee);
            }
        });

    }
    public void deleteEntity(UUID id) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (employeeRepository.existsChecksLinkedTo(id))
                    throw new ConstraintViolationException("Can`t delete the employee, as some checks are linked to him", null);
                employeeRepository.delete(id);
            }
        });

    }
    public int countEntities() {
        return employeeRepository.count();
    }
    @Nullable
    public ArrayList<Employee> getAllBy(String empl_surname,
                                           String empl_role,
                                           String phone_number){
        return  employeeRepository.getAllBy(empl_role, empl_surname, phone_number);
    }

    public List<String> getAllRoles(){
        return employeeRepository.getRoles();
    }
    public Employee findById(UUID id){
        return employeeRepository.findById(id);
    }
    public List<Employee> getCashiersWithNumberOfChecks(){
        return employeeRepository.getCashiersWithNumberOfChecks();
    }
}
