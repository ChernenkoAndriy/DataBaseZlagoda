package com.example.demo.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import database_manegment.database_entities.Employee;

@Route("manager")
public class ManagerInitialView extends AppLayout {
    private Grid<Employee> workers;
    private SideNav sideNav;
    private TextField filterField;
    private Dialog addEmployeeDialog;

    public ManagerInitialView() {
        workers = new Grid<>(Employee.class);
        addEmployeeDialog = new Dialog();
        AddEmployeeForm addEmployeeForm = new AddEmployeeForm();
        addEmployeeForm.setWidthFull();
        addEmployeeDialog.setWidth("70%");
        addEmployeeDialog.setHeight("auto");
        addEmployeeDialog.add(addEmployeeForm);
        addEmployeeDialog.setModal(true);


        // Верхня панель з кнопками
        HorizontalLayout bar = new HorizontalLayout();
        filterField = new TextField();
        filterField.setPlaceholder("Фільтр за ім'ям або прізвищем");
        filterField.addValueChangeListener(e -> filterEmployees(e.getValue()));

        Button addButton = new Button("Додати працівника", e -> addEmployeeDialog.open());
        bar.add(filterField, addButton);
        bar.setSpacing(true);

        // Верхня панель
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Zlagoda");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        sideNav = new SideNav();
        Scroller scroller = new Scroller(sideNav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);

        VerticalLayout content = new VerticalLayout(bar, workers);
        content.setSizeFull();
        setContent(content);

        sideNav.setWidth("250px");
        sideNav.addItem(new SideNavItem("Працівники"));
        sideNav.addItem(new SideNavItem("Клієнти"));
        sideNav.addItem(new SideNavItem("Товари"));
        sideNav.addItem(new SideNavItem("Категорії товарів"));
        sideNav.addItem(new SideNavItem("Види товарів"));
        sideNav.addItem(new SideNavItem("Чеки"));
    }

    private void filterEmployees(String filter) {
        workers.setItems();
    }
}
