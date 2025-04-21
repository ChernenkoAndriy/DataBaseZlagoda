package com.example.demo.views.components.ProductPageComponents;

import com.example.demo.views.events.CloseEvent;
import com.example.demo.views.events.DeleteEvent;
import com.example.demo.views.events.SaveEvent;
import com.example.demo.views.repositories.database_entities.Category;
import com.example.demo.views.repositories.database_entities.Product;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class ProductForm extends Dialog{
    protected final Binder<Product> binder;
    protected Button deleteButton = new Button("Delete");
    protected Button closeButton = new Button("Cancel");
    protected final Button saveButton = new Button("Save");
    protected TextField productNameField = new TextField("Product name");
    protected ComboBox<Category> categoryChooser = new ComboBox<>("Category");
    protected TextArea description = new TextArea("Description");

    public ProductForm(List<Category> categories) {
        categoryChooser.setItems(categories);
        categoryChooser.setItemLabelGenerator(category ->
                category.getCategory_name() != null ? category.getCategory_name() : "");

        this.binder = new Binder<>(Product.class);
        configureUI();
        configureBinder();
        this.setWidth("70%");
    }
    protected void configureBinder() {
        binder.forField(productNameField)
                .asRequired("Product name is required")
                .bind(Product::getProduct_name, Product::setProduct_name);

        binder.forField(description)
                .bind(Product::getCharacteristics, Product::setCharacteristics);

        binder.forField(categoryChooser)
                .asRequired("Category is required")
                .bind(Product::getCategory, Product::setCategory);
    }

    protected void configureUI() {
        categoryChooser.setAllowCustomValue(false);
        saveButton.addThemeName("primary");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.setWidth("33%");
        deleteButton.setWidth("33%");
        closeButton.setWidth("33%");

        FormLayout formLayout = new FormLayout();
        formLayout.add(productNameField, categoryChooser, description, new Span());

        HorizontalLayout buttons = new HorizontalLayout(FlexComponent.JustifyContentMode.CENTER);
        buttons.setWidth("100%");
        buttons.add(saveButton, deleteButton, closeButton);
        this.add(formLayout);
        this.add(buttons);
        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        saveButton.addClickListener(event -> validateAndSave()); // <1>
        deleteButton.addClickListener(event -> fireEvent(new DeleteProductEvent(this, binder.getBean()))); // <2>
        closeButton.addClickListener(event -> fireEvent(new CloseProductEvent(this))); // <3>
    }
    public void setProduct(Product e) {
        binder.setBean(e);
    }
    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveProductEvent(this, binder.getBean())); // <6>
        }
    }
    public void addDeleteListener(ComponentEventListener<DeleteProductEvent> listener) {
        addListener(DeleteProductEvent.class, listener);
    }
    public void addSaveListener(ComponentEventListener<SaveProductEvent> listener) {
        addListener(SaveProductEvent.class, listener);
    }
    public void addCloseListener(ComponentEventListener<CloseProductEvent> listener) {
        addListener(CloseProductEvent.class, listener);
    }

    public void setInavalidName() {
        productNameField.setInvalid(true);
        productNameField.setErrorMessage("Such name already exists");
    }

    public static class CloseProductEvent extends CloseEvent<ProductForm> {
        public CloseProductEvent(ProductForm employeeForm) {
            super(employeeForm);
        }
    }
    public static class SaveProductEvent extends SaveEvent<ProductForm, Product>{
        public SaveProductEvent(ProductForm employeeForm, Product e) {
            super(employeeForm, e);
        }
    }

    public static class DeleteProductEvent extends DeleteEvent<ProductForm, Product>{

        public DeleteProductEvent(ProductForm employeeForm, Product e) {
            super(employeeForm, e);
        }
    }

}
