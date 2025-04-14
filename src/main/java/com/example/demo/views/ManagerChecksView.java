package com.example.demo.views;

import com.example.demo.views.components.CheckPageComponents.CheckForm;
import com.example.demo.views.components.CheckPageComponents.CheckToolbar;
import com.example.demo.views.components.CheckPageComponents.CheckTable;
import com.example.demo.views.services.CheckService;
import com.example.demo.views.repositories.database_entities.Check;
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
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Route(value = "checks", layout = ManagerLayout.class)
@SpringComponent
@Scope("prototype")
@PageTitle("Checks | ZLAGODA")
public class ManagerChecksView extends AppLayout {

    protected CheckTable table;
    protected CheckToolbar bar;
    protected CheckService service;
    protected CheckForm checkForm;

    public ManagerChecksView(CheckService service) {
        this.service = service;
        this.checkForm = new CheckForm();
        this.table = new CheckTable(service);
        this.bar = new CheckToolbar();
        configureContent();
    }

    private void configureContent() {
        bar.setWidth("100%");
        table.setMinWidth("130%");
        table.asSingleSelect().addValueChangeListener(event ->
                editCheck(event.getValue()));

        VerticalLayout tableContainer = new VerticalLayout(table);
        tableContainer.setSizeFull();
        tableContainer.setPadding(false);
        tableContainer.getStyle().set("overflow", "auto");

        VerticalLayout content = new VerticalLayout(bar, tableContainer);
        content.setSizeFull();
        setContent(content);

        checkForm.addSaveListener(this::saveCheck);
        checkForm.addDeleteListener(this::deleteCheck);
        checkForm.addCloseListener(e -> closeEditor());

        bar.getAddButton().addClickListener(e -> addCheck());
        bar.addUpdateListener(e -> updateList());
    }

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

    private void closeEditor() {
        checkForm.setCheck(null);
        checkForm.close();
    }

    private void updateList() {
        table.setItems(bar.getAllByFilters(service));
    }

    private void saveCheck(CheckForm.SaveCheckEvent event) {
        try {
            Check c = event.getCheck();
            if (c.getId() == null) {
                service.addEntity(c);
            } else {
                service.updateEntity(c);
            }
            updateList();
            closeEditor();
        } catch (ConstraintViolationException e) {
            checkForm.setInvalidData();
        }
    }

    private void deleteCheck(CheckForm.DeleteCheckEvent event) {
        try {
            service.deleteEntity(event.getCheck().getId());
            updateList();
            closeEditor();
        } catch (ConstraintViolationException er) {
            showErrorNotification(er.getMessage());
        }
    }

    private void editCheck(Check check) {
        if (check == null) {
            closeEditor();
        } else {
            checkForm.setCheck(check);
            checkForm.open();
            addClassName("editing");
        }
    }

    private void addCheck() {
        table.asSingleSelect().clear();
        editCheck(new Check());
    }
}
