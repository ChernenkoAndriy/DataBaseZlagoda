package com.example.demo.views;

import com.example.demo.views.components.EmployeePageComponents.EmployeeForm;
import com.example.demo.views.components.EmployeePageComponents.EmployeeToolbar;
import com.example.demo.views.components.EmployeePageComponents.EmployeeTable;
import com.example.demo.views.services.EmployeeService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.example.demo.views.repositories.database_entities.Employee;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
//скопіюйте аннотації так як вони є, вони потрібні для роботи сторінки
@Route(value = "manager", layout = MainLayout.class)
//в аннотації Route value це назва сторінки, яка мусить бути такою як в ManagerLayout
//там бокова панель яка направляє на сторінку за цим value
//layout = ManagerLayout.class потрібна щоб на сторінку автоматично додалась меню та заголовок,
@SpringComponent
@Scope("prototype")
@RolesAllowed("ROLE_MANAGER")
@PageTitle("Employees | ZLAGODA")
public class EmployeeView extends AppLayout {
    //це компонент табличка, його дивитись окремо
    protected EmployeeTable table;
    //це панель з елементами над таблицею, теж дивитись
    protected EmployeeToolbar bar;
    //цей клас буде створений для виконання запитів до бд
    protected EmployeeService service;
    //це формочка що додається на сторінку при редагуванні, теж подивіться
    protected EmployeeForm employeeForm;
    //для кожної сторінки набір таких класів буде один і той самий

    //клас сервісу створиться автоматично програмою, тому клас можна створити з пустим конструктором
    //дивіться сервіс щоб зрозуміти
    public EmployeeView(EmployeeService service) {
        //метод  отримує список посад
        List<String> roles = service.getAllRoles();
        //ініціалізація
        this.employeeForm = new EmployeeForm(roles);
        this.service = service;
        this.table = new EmployeeTable(service);
        roles=service.getAllRoles();
        this.bar = new EmployeeToolbar(roles);
        configureContent();
        bar.getCheckStatistic().addClickListener(e -> {
                    bar.updateForm(service.getCashiersWithNumberOfChecks());
                    bar.openForm();
                });
    }
    //тут модифікація компонентів
    private void configureContent() {
        bar.setWidth("100%");
        table.setMinWidth("130%");
        table.asSingleSelect().addValueChangeListener(event ->
                editEmployee(event.getValue()));
        VerticalLayout tableContainer = new VerticalLayout(table);
        tableContainer.setSizeFull();
        tableContainer.setPadding(false);
        tableContainer.getStyle().set("overflow", "auto");
        VerticalLayout content = new VerticalLayout(bar, tableContainer);
        content.setSizeFull();
        setContent(content);
        //назначаються слухачі подій
        //розберіть що тут відбувається коли прочитаєте решту класів
        employeeForm.addSaveListener(this::saveEmployee);
        employeeForm.addDeleteListener(this::deleteEmployee);
        employeeForm.addCloseListener(e -> closeEditor());
        //назначаємо на кнопку відкриття пустої ворми
        bar.getAddButton().addClickListener(e -> addEmployee());
        //назначаємо слухача на оновлення сторінки(
        // подія update викликається коли міняються значення фільтрів)
        bar.addUpdateListener(e -> updateList());
    }
    //метод показує поверх сторінки повідомлення про помилку(коли не можемо видалити
    // працівника наприклад)
    private void showErrorNotification(String message) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        Div text = new Div(new Text(message));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Close");
        closeButton.addClickListener(event -> notification.close());
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        notification.setDuration(7000);
        notification.add(layout);
        notification.open();
    }
    //закриває формочку, викликається слухачем події closed
    private void closeEditor() {
        employeeForm.setEmployee(null);
        employeeForm.close();
    }
    //оновлює список, оновлення проходить завжди з фільтрацією компонентів toolbar
    private void updateList() {
    table.setItems(bar.getAllByfilters(service));
    }
    //отримує employee з події save і зберігає через сервіс
    //оновлює якщо він вже має ключ, тобто уже додавався до бд
    //додає наново якщо такого немає
    private void saveEmployee(EmployeeForm.SaveEmployeeEvent event) {
        try {
            Employee e = event.getEntity();
            if (e.getId() == null) {
                String password = event.getPassword();
                String login = event.getLogin();
               service.addEntity(e, login, password);
            } else {
               service.updateEntity(e);
            }
            updateList();
            closeEditor();
        }catch (ConstraintViolationException e){
            employeeForm.setInvalidNumber();
        }catch (DuplicateKeyException e){
            showErrorNotification(e.getMessage());
            employeeForm.setInvalidLogin();
        }
    }
    //отримує employee з події delete і видаляє через сервіс
    //використовує метод IEntity getId() для отримання id
    //подивіться цей інтерфейс
    private void deleteEmployee(EmployeeForm.DeleteEmployeeEvent event) {
        try {
            service.deleteEntity(event.getEntity().getId());
            updateList();
            closeEditor();
        } catch (ConstraintViolationException er) {
            showErrorNotification(er.getMessage());
        }
    }
    //отримує employee з події edit та викликає відкриття форми
    private void editEmployee(Employee e) {
        if (e == null) {
            closeEditor();
        } else {
            employeeForm.setEmployee(e);
            employeeForm.open();
            addClassName("editing");
        }
    }
    //відкриває форму але передає пустого empl для редагування, щоб потім зберегти нового
    private void addEmployee(){
        table.asSingleSelect().clear();
        editEmployee(new Employee());
    }
}
