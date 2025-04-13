package com.example.demo.views.components.EmployeePageComponents;

import com.example.demo.views.events.CloseEvent;
import com.example.demo.views.events.DeleteEvent;
import com.example.demo.views.events.SaveEvent;
import com.example.demo.views.repositories.database_entities.Employee;
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
//формочка що з'являється на сторінці
//розширює діалог, що є пустою панеллю наяку можна додати об'єкти
public class EmployeeForm extends Dialog{
    //це дуже важливе поле
    //воно допомагає зв'язати поля Employee та об'єкти формочки
    //як це робиться дивіться configureBinder()
    protected final Binder<Employee> binder;
    protected Button deleteButton = new Button("Delete");
    protected Button closeButton = new Button("Cancel");
    protected final Button saveButton = new Button("Save");
    protected TextField nameField = new TextField("Name");
    protected TextField surnameField = new TextField("Surname");
    protected TextField patronymic = new TextField("Patronymic");
    protected ComboBox<String> rolechooser = new ComboBox<>("Role");
    protected BigDecimalField salaryField = new BigDecimalField("Salary");
    protected DatePicker dateofBirth = new DatePicker("Date of birth");;
    protected DatePicker dateofStart = new DatePicker("Date of start");
    protected TextField phoneField = new TextField("Phone Number");
    protected TextField cityField = new TextField("City");
    protected TextField streetField = new TextField("Street");
    protected TextField zipcode = new TextField("Zip Code");

    public EmployeeForm(List<String> roles) {
        rolechooser.setItems(roles);
        this.binder = new Binder<>(Employee.class);
        configureUI();
        configureBinder();
        this.setWidth("70%");
    }
    protected void configureBinder() {
        //тобто для кожного поля Employee береться геттер і сеттер, що є обов'язковим
        //для кожного компоненту форми тут необхідно налаштовувати валідацію
        binder.forField(nameField)
                .asRequired("Name is required")//робить щоб поле було обов'язковим
                .withValidator(new StringLengthValidator(
                        "Name must be between 1 and 50 characters", 1, 50)) // фільтр довжини
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
                .withValidator(phone -> phone.matches("\\+?\\d{12}"
                ), "Invalid format. Use +XXXXXXXXXXXX")
                .bind(Employee::getPhone_number, Employee::setPhone_number);

        binder.forField(rolechooser)
                .asRequired("Role is required")
                .bind(Employee::getEmpl_role, Employee::setEmpl_role);

        binder.forField(dateofBirth)
                .asRequired("Date of birth is required")
                .withValidator(dob -> dob.isBefore(LocalDate.now().minusYears(18)), "Employee must be at least 18 years old")
                .bind(Employee::getDate_of_birth, Employee::setDate_of_birth);

        binder.forField(dateofStart)
                .asRequired("Start date is required")
                .withValidator(start -> {
                    LocalDate dob = binder.getBean().getDate_of_birth();
                    return dob != null && start.isAfter(dob.plusYears(18));
                }, "Employee must be at least 18 years old at the moment of start")
                .bind(Employee::getDate_of_start, Employee::setDate_of_start);

        binder.forField(salaryField)
                .asRequired("Salary is required")
                .withValidator(createBigDecimalValidator())
                .bind(Employee::getSalary, Employee::setSalary);

        binder.forField(cityField)
                .asRequired("City is required")
                .withValidator(new StringLengthValidator("City must be between 2 and 50 characters", 2, 50))
                .bind(Employee::getCity, Employee::setCity);

        binder.forField(streetField)
                .asRequired("Street is required")
                .withValidator(new StringLengthValidator("Street must be between 2 and 100 characters", 2, 100))
                .bind(Employee::getStreet, Employee::setStreet);

        binder.forField(zipcode)
                .asRequired("Zip Code is required")
                .withValidator(zip -> zip.matches("\\d{9}"), "Invalid zip code format")
                .bind(Employee::getZip_code, Employee::setZip_code);
    }
    protected void configureUI() {
        rolechooser.setAllowCustomValue(false);
        saveButton.addThemeName("primary");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.setWidth("33%");
        deleteButton.setWidth("33%");
        closeButton.setWidth("33%");

        FormLayout formLayout = new FormLayout();
        formLayout.add(nameField, surnameField, patronymic, rolechooser,
                dateofBirth, dateofStart, phoneField, cityField,
                streetField, zipcode, salaryField, new Span());

        HorizontalLayout buttons = new HorizontalLayout(FlexComponent.JustifyContentMode.CENTER);
        buttons.setWidth("100%");
        buttons.add(saveButton, deleteButton, closeButton);
        this.add(formLayout);
        this.add(buttons);

        //тут ми назначаємо лістенери
        //деякі з них використовують fireevent що створює
        // глобальну подію на яку можна реагувати з іншого класу
        //в employeeview ми можемо хендлити вже саме ці події
        //створюються вони нижче
        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        saveButton.addClickListener(event -> validateAndSave()); // <1>
        deleteButton.addClickListener(event -> fireEvent(new DeleteEmployeeEvent(this, binder.getBean()))); // <2>
        closeButton.addClickListener(event -> fireEvent(new CloseEmployeeEvent(this))); // <3>
    }
    //передає параметри в біндер
    public void setEmployee(Employee e) {
        binder.setBean(e);
    }
    //біндер сам проходить по полях і перевіряє правильність формату введення даних і логічні помилки
    //такі речі як наприклад збіг телефонів тут потрібно хендлити ззовні
    //це робиться у view
    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEmployeeEvent(this, binder.getBean())); // <6>
        }
    }
    //валідатор для зарплат
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
    //методи які визначають логіку лісенера ззовні класу
    public void addDeleteListener(ComponentEventListener<DeleteEmployeeEvent> listener) {
        addListener(DeleteEmployeeEvent.class, listener);
    }
    public void addSaveListener(ComponentEventListener<SaveEmployeeEvent> listener) {
        addListener(SaveEmployeeEvent.class, listener);
    }
    public void addCloseListener(ComponentEventListener<CloseEmployeeEvent> listener) {
        addListener(CloseEmployeeEvent.class, listener);
    }
//приклад створення подій
    //в параметр береться джерело виклику і інколи тип значення що передається
    public static class CloseEmployeeEvent extends CloseEvent<EmployeeForm> {
        public CloseEmployeeEvent(EmployeeForm employeeForm) {
            super(employeeForm);
        }
    }
    public static class SaveEmployeeEvent extends SaveEvent<EmployeeForm, Employee>{
        public SaveEmployeeEvent(EmployeeForm employeeForm, Employee e) {
            super(employeeForm, e);
        }
    }

    public static class DeleteEmployeeEvent extends DeleteEvent<EmployeeForm, Employee>{

        public DeleteEmployeeEvent(EmployeeForm employeeForm, Employee e) {
            super(employeeForm, e);
        }
    }
    public void setInvalidNumber(){
        phoneField.setInvalid(true);
        phoneField.setErrorMessage("Such phone number is already registered");
    }
}
