package com.example.demo.views;

import com.example.demo.views.components.CategoryPageComponents.CategoryForm;
import com.example.demo.views.components.CategoryPageComponents.CategoryTable;
import com.example.demo.views.components.CategoryPageComponents.CategoryToolbar;
import com.example.demo.views.repositories.database_entities.Category;
import com.example.demo.views.services.CategoryService;
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
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Scope;

@Route(value = "categories", layout = MainLayout.class)
@SpringComponent
@RolesAllowed("ROLE_MANAGER")
@Scope("prototype")
@PageTitle("Categories | ZLAGODA")
public class CategoryView extends AppLayout {
    protected CategoryTable table;
    protected CategoryToolbar bar;
    protected CategoryService service;
    protected CategoryForm categoryForm;
    public CategoryView(CategoryService service) {
        this.categoryForm = new CategoryForm();
        this.service = service;
        this.table = new CategoryTable(service);
        this.bar = new CategoryToolbar();
        configureContent();
    }
    private void configureContent() {
        bar.setWidth("100%");
        table.setMinWidth("100%");
        table.asSingleSelect().addValueChangeListener(event ->
                editCategory(event.getValue()));
        VerticalLayout tableContainer = new VerticalLayout(table);
        tableContainer.setSizeFull();
        tableContainer.setPadding(false);
        tableContainer.getStyle().set("overflow", "auto");
        VerticalLayout content = new VerticalLayout(bar, tableContainer);
        content.setSizeFull();
        setContent(content);
        categoryForm.addSaveListener(this::saveCategory);
        categoryForm.addDeleteListener(this::deleteCategory);
        categoryForm.addCloseListener(e -> closeEditor());
        bar.getAddButton().addClickListener(e -> addCategory());
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
        categoryForm.setCategory(null);
        categoryForm.close();
    }
    private void updateList() {
        table.setItems(bar.getAllByfilters(service));
    }
    private void saveCategory(CategoryForm.SaveCategoryEvent event) {
        try {
            Category e = event.getEntity();
            if (e.getId() == null) {
                service.addEntity(e);
            } else {
                service.updateEntity(e);
            }
            updateList();
            closeEditor();
        }catch (ConstraintViolationException e){
            showErrorNotification(e.getMessage());
        }
    }
    private void deleteCategory(CategoryForm.DeleteCategoryEvent event) {
        try {
            service.deleteEntity(event.getEntity().getId());
            updateList();
            closeEditor();
        } catch (ConstraintViolationException er) {
            showErrorNotification(er.getMessage());
        }
    }
    private void editCategory(Category e) {
        if (e == null) {
            closeEditor();
        } else {
            categoryForm.setCategory(e);
            categoryForm.open();
            addClassName("editing");
        }
    }
    private void addCategory(){
        table.asSingleSelect().clear();
        editCategory(new Category());
    }
}
