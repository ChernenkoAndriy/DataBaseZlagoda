package com.example.demo.views.components;

import com.example.demo.views.viewmanagers.MCService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span; // Використовуємо Span замість Label
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class CustomerToolbar extends HorizontalLayout {
    private final TextField filterField;
    private Dialog addCustomerDialog;
    private final Button printButton;
    private final Button exportButton;
    private final Button addButton;
    private final Span discountLabel; // Замінили Label на Span
    private final NumberField discountField;
    private final MCService mcService;

    public CustomerToolbar(MCService mcService) {
        this.mcService = mcService;
        this.setAlignItems(Alignment.CENTER);
        // Поле пошуку
        filterField = new TextField();
        filterField.setPlaceholder("Find customer");
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.setWidth("30%");

        // Кнопка додавання
        addButton = new Button("Add customer", ev -> {
            initializeDialogueForm();
            customizeDialogue(addCustomerDialog);
            addCustomerDialog.open();
        });
        addButton.addThemeName("primary");
        addButton.setWidth("20%");

        // Поле для фільтрації за знижкою
        discountLabel = new Span("Discount filter:"); // Використовуємо Span
        discountField = new NumberField();
        discountField.setPlaceholder("Enter discount");
        discountField.setMin(0);
        discountField.setMax(100);
        discountField.setStep(1);
        discountField.setWidth("13%");
        Span percent = new Span("%");
        discountField.setSuffixComponent(percent);
        discountField.addValueChangeListener(event -> {
            double discount = event.getValue();

        });

        // Кнопки
        exportButton = new Button("Export");
        exportButton.addThemeName("primary");
        exportButton.setWidth("15%");

        printButton = new Button("Print");
        printButton.setWidth("15%");
        printButton.addThemeName("primary");

        // Створюємо Layout для правої частини тулбара
        HorizontalLayout rightLayout = new HorizontalLayout(exportButton, printButton);
        rightLayout.setAlignItems(FlexComponent.Alignment.END);  // Вирівнюємо кнопки праворуч
        rightLayout.setSpacing(true);  // Додаємо відстань між кнопками

        // Додаємо елементи в тулбар
        add(filterField, discountLabel, discountField, addButton);
        add(rightLayout);
    }

    private void initializeDialogueForm() {
        addCustomerDialog = new Dialog();
        CustomerForm cf = new CustomerForm(mcService);
        addCustomerDialog.add(cf);
    }

    private void customizeDialogue(Dialog d){
        d.setWidth("70%");
        d.setHeight("auto");
        d.setModal(true);
    }
}
