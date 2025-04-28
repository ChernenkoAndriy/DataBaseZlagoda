package com.example.demo.views.components.EmployeePageComponents;

import com.example.demo.views.events.CloseEvent;
import com.example.demo.views.events.DeleteEvent;
import com.example.demo.views.events.SaveEvent;
import com.example.demo.repositories.database_entities.Employee;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeForm extends Dialog {
    protected final Binder<Employee> binder;
    protected Button deleteButton = new Button("Delete");
    protected Button closeButton = new Button("Cancel");
    protected final Button saveButton = new Button("Save");
    protected TextField nameField = new TextField("Name");
    protected TextField surnameField = new TextField("Surname");
    protected TextField patronymic = new TextField("Patronymic");
    protected ComboBox<String> rolechooser = new ComboBox<>("Role");
    protected BigDecimalField salaryField = new BigDecimalField("Salary");
    protected DatePicker dateofBirth = new DatePicker("Date of birth");
    protected DatePicker dateofStart = new DatePicker("Date of start");
    protected TextField phoneField = new TextField("Phone Number");
    protected TextField cityField = new TextField("City");
    protected TextField streetField = new TextField("Street");
    protected TextField zipcode = new TextField("Zip Code");
    protected TextField loginField = new TextField("Login");
    protected TextField passwordField = new TextField("Password");

    public EmployeeForm(List<String> roles) {
        rolechooser.setItems(roles);
        this.binder = new Binder<>(Employee.class);
        configureUI();
        configureBinder();
        this.setWidth("70%");
    }

    protected void configureBinder() {
        binder.forField(nameField)
                .asRequired("Name is required")
                .withValidator(new StringLengthValidator("Name must be between 1 and 50 characters", 1, 50))
                .bind(Employee::getEmpl_name, Employee::setEmpl_name);

        binder.forField(surnameField)
                .asRequired("Surname is required")
                .withValidator(new StringLengthValidator("Surname must be between 1 and 50 characters", 1, 50))
                .bind(Employee::getEmpl_surname, Employee::setEmpl_surname);

        binder.forField(patronymic)
                .withNullRepresentation("")
                .withValidator(value -> value == null || value.length() <= 50,
                        "Patronymic must be less than 50 characters")
                .bind(Employee::getEmpl_patronymic, Employee::setEmpl_patronymic);

        binder.forField(phoneField)
                .asRequired("Phone number is required")
                .withValidator(phone -> phone.matches("\\+?\\d{12}"),
                        "Invalid format. Use +XXXXXXXXXXXX")
                .bind(Employee::getPhone_number, Employee::setPhone_number);

        binder.forField(rolechooser)
                .asRequired("Role is required")
                .bind(Employee::getEmpl_role, Employee::setEmpl_role);

        dateofBirth.setMax(LocalDate.now());
        dateofBirth.setMin(LocalDate.now().minusYears(100));
        dateofStart.setMax(LocalDate.now());
        dateofStart.setMin(LocalDate.now().minusYears(100));


        binder.forField(dateofBirth)
                .asRequired("Date of birth is required")
                .withValidator(dob -> dob.isBefore(LocalDate.now().minusYears(18)), "Employee must be at least 18 years old")
                .bind(Employee::getDate_of_birth, Employee::setDate_of_birth);

        binder.forField(dateofStart)
                .asRequired("Start date is required")
                .withValidator(start -> {
                    LocalDate dob = dateofBirth.getValue();
                    return dob != null && start.isAfter(dob.plusYears(18));
                }, "Employee must be at least 18 years old at the moment of start")
                .bind(Employee::getDate_of_start, Employee::setDate_of_start);

        binder.forField(salaryField)
                .asRequired("Salary is required")
                .withValidator(createBigDecimalValidator())
                .bind(Employee::getSalary, Employee::setSalary);

        binder.forField(cityField)
                .asRequired("City is required")
                .withValidator(city -> city == null || city.matches("^[a-zA-Z\\s'-]+$"),
                        "City must contain only English letters")
                .withValidator(new StringLengthValidator("City must be between 1 and 50 characters", 1, 50))
                .bind(Employee::getCity, Employee::setCity);

        binder.forField(streetField)
                .asRequired("Street is required")
                .withValidator(new StringLengthValidator("Street must be between 1 and 100 characters", 1, 100))
                .bind(Employee::getStreet, Employee::setStreet);

        binder.forField(zipcode)
                .asRequired("Zip Code is required")
                .withValidator(zip -> zip.matches("\\d{9}"), "Invalid zip code format, enter 9 digits")
                .bind(Employee::getZip_code, Employee::setZip_code);

        if (loginField.isVisible()) {
            binder.forField(loginField)
                    .asRequired("Login is required")
                    .withValidator(new StringLengthValidator("Login must be between 5 and 15 characters", 5, 15))
                    .withValidator(login -> login.matches("^[a-zA-Z]+$"), "Login must contain only Latin letters")
                    .bind(e -> null, (e, value) -> {});
        }

        if (passwordField.isVisible()) {
            binder.forField(passwordField)
                    .asRequired("Password is required")
                    .withValidator(new StringLengthValidator("Password must be between 5 and 15 characters", 5, 15))
                    .withValidator(password -> password.matches("^[a-zA-Z0-9]+$"), "Password must contain only Latin letters and digits")
                    .bind(e -> null, (e, value) -> {});
        }

    }
    protected void configureUI() {
        rolechooser.setAllowCustomValue(false);
        saveButton.addThemeName("primary");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.setWidth("33%");
        deleteButton.setWidth("33%");
        closeButton.setWidth("33%");

        FormLayout formLayout = new FormLayout();
        formLayout.add(nameField, surnameField, patronymic, rolechooser,
                dateofBirth, dateofStart, phoneField, cityField,
                streetField, zipcode, salaryField, loginField, passwordField, new Span());

        HorizontalLayout buttons = new HorizontalLayout(FlexComponent.JustifyContentMode.CENTER);
        buttons.setWidth("100%");
        buttons.add(saveButton, deleteButton, closeButton);

        this.add(formLayout);
        this.add(buttons);
        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEmployeeEvent(this, binder.getBean())));
        closeButton.addClickListener(event -> fireEvent(new CloseEmployeeEvent(this)));
    }
    public void setEmployee(Employee e) {
        if(e != null) {
            binder.removeBinding(loginField);
            binder.removeBinding(passwordField);

            if (e.getId_employee() == null) {
                loginField.setVisible(true);
                passwordField.setVisible(true);

                binder.forField(loginField)
                        .asRequired("Login is required")
                        .withValidator(new StringLengthValidator("Login must be between 5 and 15 characters", 5, 15))
                        .withValidator(login -> login.matches("^[a-zA-Z]+$"), "Login must contain only Latin letters")
                        .bind(emp -> null, (emp, value) -> {
                        });

                binder.forField(passwordField)
                        .asRequired("Password is required")
                        .withValidator(new StringLengthValidator("Password must be between 5 and 15 characters", 5, 15))
                        .withValidator(password -> password.matches("^[a-zA-Z0-9]+$"), "Password must contain only Latin letters and digits")
                        .bind(emp -> null, (emp, value) -> {
                        });
            } else {
                loginField.setVisible(false);
                passwordField.setVisible(false);
            }
        }
        binder.setBean(e);
    }
    private void validateAndSave() {
        if (binder.isValid()) {
            Employee res = binder.getBean();
            String password =null;
            String login = null;
            if(res.getId_employee() == null) {
                password = passwordField.getValue();
                login = loginField.getValue();
            }
            fireEvent(new SaveEmployeeEvent(this, res, login, password));
        }
    }
    private Validator<BigDecimal> createBigDecimalValidator() {
        Pattern pattern = Pattern.compile("^[0-9]\\d{0,8}(\\.\\d{1,4})?$");
        return (value, context) -> {
            if (value == null) {
                return ValidationResult.error("Salary is required");
            }
            if (!pattern.matcher(value.toPlainString()).matches()) {
                return ValidationResult.error("Invalid format (1-9 digits, optional . up to 4 decimals)");
            }
            return ValidationResult.ok();
        };
    }
    public void addDeleteListener(ComponentEventListener<DeleteEmployeeEvent> listener) {
        addListener(DeleteEmployeeEvent.class, listener);
    }
    public void addSaveListener(ComponentEventListener<SaveEmployeeEvent> listener) {
        addListener(SaveEmployeeEvent.class, listener);
    }
    public void addCloseListener(ComponentEventListener<CloseEmployeeEvent> listener) {
        addListener(CloseEmployeeEvent.class, listener);
    }
    public void setInvalidLogin() {
        loginField.setInvalid(true);
        loginField.setErrorMessage("Such login alredy exists");
    }

    public static class CloseEmployeeEvent extends CloseEvent<EmployeeForm> {
        public CloseEmployeeEvent(EmployeeForm employeeForm) {
            super(employeeForm);
        }
    }

    public static class SaveEmployeeEvent extends SaveEvent<EmployeeForm, Employee> {
        private final String login;
        private final String password;

        public SaveEmployeeEvent(EmployeeForm employeeForm, Employee e, String login, String password) {
            super(employeeForm, e);
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class DeleteEmployeeEvent extends DeleteEvent<EmployeeForm, Employee> {
        public DeleteEmployeeEvent(EmployeeForm employeeForm, Employee e) {
            super(employeeForm, e);
        }
    }

    public void setInvalidNumber() {
        phoneField.setInvalid(true);
        phoneField.setErrorMessage("Such phone number is already registered");
    }
}
