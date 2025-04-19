package com.example.demo.views.components.CategoryPageComponents;

import com.example.demo.views.events.CloseEvent;
import com.example.demo.views.events.DeleteEvent;
import com.example.demo.views.events.SaveEvent;
import com.example.demo.views.repositories.database_entities.Category;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
public class CategoryForm extends Dialog{
    protected final Binder<Category> binder;
    protected Button deleteButton = new Button("Delete");
    protected Button closeButton = new Button("Cancel");
    protected final Button saveButton = new Button("Save");
    protected TextField nameField = new TextField("Category name");
    public CategoryForm() {
        this.binder = new Binder<>(Category.class);
        configureUI();
        configureBinder();
        this.setWidth("70%");
    }
    protected void configureBinder() {
        binder.forField(nameField)
                .asRequired("Name is required")
                .withValidator(new StringLengthValidator(
                        "Name must be between 1 and 50 characters", 1, 50))
                .bind(Category::getCategory_name, Category::setCategory_name);
    }
    protected void configureUI() {
        saveButton.addThemeName("primary");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.setWidth("33%");
        deleteButton.setWidth("33%");
        closeButton.setWidth("33%");

        FormLayout formLayout = new FormLayout();
        formLayout.add(nameField, new Span());

        HorizontalLayout buttons = new HorizontalLayout(FlexComponent.JustifyContentMode.CENTER);
        buttons.setWidth("100%");
        buttons.add(saveButton, deleteButton, closeButton);
        this.add(formLayout);
        this.add(buttons);

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        saveButton.addClickListener(event -> validateAndSave()); // <1>
        deleteButton.addClickListener(event -> fireEvent(new DeleteCategoryEvent(this, binder.getBean()))); // <2>
        closeButton.addClickListener(event -> fireEvent(new CloseCategoryEvent(this))); // <3>
    }
    public void setCategory(Category e) {
        binder.setBean(e);
    }
    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveCategoryEvent(this, binder.getBean())); // <6>
        }
    }
    public void addDeleteListener(ComponentEventListener<DeleteCategoryEvent> listener) {
        addListener(DeleteCategoryEvent.class, listener);
    }
    public void addSaveListener(ComponentEventListener<SaveCategoryEvent> listener) {
        addListener(SaveCategoryEvent.class, listener);
    }
    public void addCloseListener(ComponentEventListener<CloseCategoryEvent> listener) {
        addListener(CloseCategoryEvent.class, listener);
    }
    public static class CloseCategoryEvent extends CloseEvent<CategoryForm> {
        public CloseCategoryEvent(CategoryForm employeeForm) {
            super(employeeForm);
        }
    }
    public static class SaveCategoryEvent extends SaveEvent<CategoryForm, Category> {
        public SaveCategoryEvent(CategoryForm employeeForm, Category e) {
            super(employeeForm, e);
        }
    }

    public static class DeleteCategoryEvent extends DeleteEvent<CategoryForm, Category> {

        public DeleteCategoryEvent(CategoryForm employeeForm, Category e) {
            super(employeeForm, e);
        }
    }
}
