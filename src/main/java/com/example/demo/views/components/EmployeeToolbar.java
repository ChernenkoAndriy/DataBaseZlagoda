package com.example.demo.views.components;

import com.example.demo.views.viewmanagers.MEService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class EmployeeToolbar extends HorizontalLayout {
    private TextField filterField;
    private Dialog addEmployeeDialog;
    private ComboBox<String> rolefilter;
    private Button printButton;
    private Button exportButton;
    private Button addButton;
    private MEService MEService;
    public EmployeeToolbar(MEService MEService){
        this.MEService = MEService;
        filterField = new TextField();
        filterField.setPlaceholder("Find employee by name, surname or phone number");
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.setWidth("55%");
        filterField.addValueChangeListener(e ->
                MEService.filterTable(rolefilter.getValue(), filterField.getValue()));

        addButton = new Button("Add employee", e -> {
            initializeDialogueForm();
            addEmployeeDialog.open();
        });
        addButton.addThemeName("primary");
        addButton.setWidth("20%");

        rolefilter = new ComboBox<>("", "All", "Manager", "Cashier");
        rolefilter.setValue("All");
        rolefilter.addValueChangeListener(e ->
                MEService.filterTable( rolefilter.getValue(), filterField.getValue()));

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
        add(filterField, rolefilter, addButton);
        add(rightLayout);
    }
    private void initializeDialogueForm() {
        addEmployeeDialog = new Dialog();
        AddEmployeeForm addEmployeeForm = new AddEmployeeForm(MEService, addEmployeeDialog);
        customizeDialogue(addEmployeeDialog);
        addEmployeeDialog.add(addEmployeeForm);
    }
    private void customizeDialogue(Dialog d){
        d.setWidth("70%");
        d.setHeight("auto");
        d.setModal(true);
    }
}
