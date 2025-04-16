//package com.example.demo.views.components.CustomerPageComponents;
//
//import com.example.demo.views.repositories.database_entities.CustomerCard;
//import com.example.demo.views.services.CustomerService;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.textfield.NumberField;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.shared.Registration;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//public class CustomerToolbar extends HorizontalLayout {
//
//    private final TextField surnameField = new TextField("Find customer");
//    private final NumberField discountField = new NumberField("Discount filter:");
//
//    private final Button addCustomer = new Button("Add customer");
//    private final Button export = new Button("Export");
//    private final Button print = new Button("Print");
//
//    private Consumer<Void> updateListener;
//
//    public CustomerToolbar() {
//        surnameField.setPlaceholder("Surname...");
//        discountField.setPlaceholder("Enter disc%");
//
//        surnameField.setClearButtonVisible(true);
//        discountField.setClearButtonVisible(true);
//
//        add(surnameField, discountField, addCustomer, export, print);
//
//        surnameField.addValueChangeListener(e -> fireUpdate());
//        discountField.addValueChangeListener(e -> fireUpdate());
//    }
//
//    public void addUpdateListener(Consumer<Void> listener) {
//        this.updateListener = listener;
//    }
//
//    private void fireUpdate() {
//        if (updateListener != null) updateListener.accept(null);
//    }
//
//    public Button getAddButton() {
//        return addCustomer;
//    }
//
//    public List<CustomerCard> getAllByFilters(CustomerService service) {
//        String surname = surnameField.getValue();
//        Integer percent = discountField.getValue() != null ? discountField.getValue().intValue() : null;
//        return service.getFilteredCustomers(surname.isEmpty() ? null : surname, percent);
//    }
//}
