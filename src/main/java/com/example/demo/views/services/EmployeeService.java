package com.example.demo.views.services;

import com.example.demo.views.repositories.database_entities.Employee;
import com.example.demo.views.repositories.EmployeeRepository;
import com.vaadin.hilla.Nullable;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//аннотація service дозволяє додатку автоматично створити цей об'єкт при компіляції і вставити туди де він потрібен
//використовуйте її для всіх сервісів
//сам сервіс вам не потрібно писати, це буду робити я
//просто декларуйте методи в класі що розширює AbstractService
//перший тип це сутність з якою працюємо а другий це тип Primary key
//UUID це просто формат рядка, який бд може генерувати саме
//тому при додаванні сутності нам не потрібно створювати новий ключ самим
@Service
public class EmployeeService extends AbstractService<Employee, UUID>{

    public EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(PlatformTransactionManager transactionManager, EmployeeRepository employeeRepository) {
        super(transactionManager);
        this.employeeRepository=employeeRepository;
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
}
