package com.example.demo.views.components.StoreProductPageComponents;
import com.example.demo.views.events.CloseEvent;
import com.example.demo.views.events.DeleteEvent;
import com.example.demo.views.events.SaveEvent;
import com.example.demo.views.repositories.database_entities.Product;
import com.example.demo.views.repositories.database_entities.StoreProduct;
import com.example.demo.views.services.StoreProductService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Pattern;
public class StoreProductForm extends Dialog {
    protected final Binder<StoreProduct> binder;
    protected Button deleteButton = new Button("Delete");
    protected Button closeButton = new Button("Cancel");
    protected final Button saveButton = new Button("Save");
    protected ComboBox<Product> productChooser = new ComboBox<>("Product Type");
    protected BigDecimalField sellingPrice = new BigDecimalField("Selling price");
    protected IntegerField amountField = new IntegerField("Amount");
    protected Checkbox checkBox = new Checkbox("Is prom");
    protected List<StoreProduct> storeProducts;
    private StoreProductService service;

    public StoreProductForm(List<Product> products, StoreProductService service) {
        storeProducts = service.getAllEntities();
        productChooser.setItems(products);
        productChooser.setItemLabelGenerator(Product::getProduct_name);
        this.binder = new Binder<>(StoreProduct.class);
        this.service= service;
        configureUI();
        configureBinder();
        configureListeners();
        this.setWidth("70%");
    }

    protected void configureBinder() {
        binder.forField(checkBox)
                .bind(StoreProduct::isPromotional_product, StoreProduct::setPromotional_product);

        binder.forField(sellingPrice)
                .asRequired("Selling price is required")
                .withValidator(createBigDecimalValidator())
                .bind(StoreProduct::getSelling_price, StoreProduct::setSelling_price);

        binder.forField(amountField)
                .asRequired("Amount is required")
                .bind(StoreProduct::getProducts_number, StoreProduct::setProducts_number);

        binder.forField(productChooser)
                .asRequired("Product must be selected")
                .bind(
                        storeProduct -> {
                            // Get Product by id_product (needed for editing)
                            return productChooser.getListDataView().getItems()
                                    .filter(p -> p.getId_product() == storeProduct.getId_product())
                                    .findFirst()
                                    .orElse(null);
                        },
                        (storeProduct, selectedProduct) -> {
                            if (selectedProduct != null) {
                                storeProduct.setId_product(selectedProduct.getId_product());
                                storeProduct.setProduct(selectedProduct.getProduct_name());
                            }
                        }
                );
    }

    protected void configureUI() {
        productChooser.setAllowCustomValue(false);
        saveButton.addThemeName("primary");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.setWidth("33%");
        deleteButton.setWidth("33%");
        closeButton.setWidth("33%");

        FormLayout formLayout = new FormLayout();
        formLayout.add(productChooser, sellingPrice, amountField, checkBox);

        HorizontalLayout buttons = new HorizontalLayout(FlexComponent.JustifyContentMode.CENTER);
        buttons.setWidth("100%");
        buttons.add(saveButton, deleteButton, closeButton);

        this.add(formLayout);
        this.add(buttons);
        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> {
            fireEvent(new DeleteStoreProductEvent(this, binder.getBean()));
            storeProducts = service.getAllEntities();
        });
        closeButton.addClickListener(event -> fireEvent(new CloseStoreProductEvent(this)));
    }

    public void setProduct(StoreProduct e) {
        if (e == null) {
            checkBox.setEnabled(false);
            productChooser.setEnabled(true);
            productChooser.setValue(null);
            checkBox.setValue(false);
            amountField.setValue(0);
            sellingPrice.setValue(BigDecimal.ZERO);
        } else {
            binder.setBean(e);
            sellingPrice.setEnabled(!e.isPromotional_product());
            checkBox.setValue(e.isPromotional_product());
            checkBox.setEnabled(false);
        }
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveStoreProductEvent(this, binder.getBean()));
        }
        storeProducts = service.getAllEntities();
    }

    private void configureListeners() {
        productChooser.addValueChangeListener(e -> {
            if(productChooser.getValue() != null && binder.getBean().getUPC()==null) {
                boolean containsGood = false;
                boolean containsPromGood = false;
                BigDecimal price = null;

                for (StoreProduct sp : storeProducts) {
                    if (sp.getProduct().equals(productChooser.getValue().getProduct_name()) ||
                            sp.getProduct().equals(productChooser.getValue().getProduct_name() + " prom")) {
                        if (sp.isPromotional_product()) {
                            containsPromGood = true;
                        } else {
                            price = sp.getSelling_price().multiply(BigDecimal.valueOf(0.8)).setScale(4, RoundingMode.HALF_UP);
                            containsGood = true;
                        }
                    }
                }

                if (containsGood) {
                    if (containsPromGood) {
                        sellingPrice.setValue(BigDecimal.ZERO);
                        productChooser.setInvalid(true);
                        productChooser.setErrorMessage("There are already goods for that product. Please select a different one.");
                        checkBox.setEnabled(true);
                    } else {
                        sellingPrice.setValue(price);
                        sellingPrice.setEnabled(false);
                        checkBox.setValue(true);
                        checkBox.setEnabled(false);
                    }
                } else {
                    sellingPrice.setEnabled(true);
                    sellingPrice.setValue(BigDecimal.ZERO);
                    checkBox.setEnabled(false);
                    checkBox.setValue(false);
                }
            }
        });
    }

    public void addDeleteListener(ComponentEventListener<DeleteStoreProductEvent> listener) {
        addListener(DeleteStoreProductEvent.class, listener);
    }

    public void addSaveListener(ComponentEventListener<SaveStoreProductEvent> listener) {
        addListener(SaveStoreProductEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<CloseStoreProductEvent> listener) {
        addListener(CloseStoreProductEvent.class, listener);
    }

    public static class CloseStoreProductEvent extends CloseEvent<StoreProductForm> {
        public CloseStoreProductEvent(StoreProductForm storeProductForm) {
            super(storeProductForm);
        }
    }

    public static class SaveStoreProductEvent extends SaveEvent<StoreProductForm, StoreProduct> {
        public SaveStoreProductEvent(StoreProductForm storeProductForm, StoreProduct e) {
            super(storeProductForm, e);
        }
    }

    public static class DeleteStoreProductEvent extends DeleteEvent<StoreProductForm, StoreProduct> {
        public DeleteStoreProductEvent(StoreProductForm storeProductForm, StoreProduct e) {
            super(storeProductForm, e);
        }
    }

    private Validator<BigDecimal> createBigDecimalValidator() {
        Pattern pattern = Pattern.compile("^[0-9]\\d{0,10}(\\.\\d{1,4})?$");
        return (value, context) -> {
            if (value == null) {
                return ValidationResult.error("Selling price is required");
            }
            if (!pattern.matcher(value.toPlainString()).matches()) {
                return ValidationResult.error("Invalid format (1-11 digits, optional . up to 4 decimals)");
            }
            return ValidationResult.ok();
        };
    }
}
