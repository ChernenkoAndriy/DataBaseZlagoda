package com.example.demo.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.ArrayList;

//це буде наш батьківський клас, від якого будуть наслідуватись інші сторінки(поки працюємо тільки з менеджерами, касира зробимо потім)
//він містить в собі бокове меню, що дозволяє рухатись по сторінкам
//воно готове і міняти його не треба
//також містить бар з кнопками які доведеться реалізовувати у нащадках
//і має ініціалізований об'єкт grid, який теж треба допилювати в нащадках
public class OverallView<T> extends AppLayout {
    protected Grid<T> table;
    protected SideMenu sidemenu;

    //колекція що буде відображати дані в табличці
    protected ListDataProvider<T> dataProvider;
    //колекція що буде зберігати дані в табличці
    protected ArrayList<T> data;
    protected HorizontalLayout bar;
//в конструктор ми передаємо тип класу для таблиці. наприклад Employee.class, якщо працюємо з працівниками на сторінці
    public OverallView(Class<T> type) {
        initializeSideMenu();
        initializeToolbar();
        initializeNavbar();
        initializeContent(type);
        modifyTable();
        modifyNavbar();
        modifyToolbar();
    }
//метод створює об'єкт який просто викликає додає і налаштовує бокову панельку що виїжджає на кнопці
    private void initializeSideMenu() {
        sidemenu = new SideMenu();
        Scroller scroller = new Scroller(sidemenu);
        scroller.getElement().getStyle().set("scrollbar-width", "none");
        scroller.setClassName(LumoUtility.Padding.SMALL);
        addToDrawer(scroller);
        sidemenu.setWidth("250px");
    }
//цей метод потрібно оверрайдити щоб задати таблицю для конкретної сторінки
    protected void modifyTable(){};
//ініціалізує тулбар
    private void initializeToolbar() {
        bar = new HorizontalLayout();
        bar.setSpacing(true);
    }
    //ми оверрайдимо цей метод у класах нащадках де ми мусимо додавати кнопочки для роботи з таблицею та решти
    protected void modifyToolbar(){}
    //ініціалізує заголовок додатка
    private void initializeNavbar() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Zlagoda");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
        addToNavbar(toggle, title);
    }
//ініціалізація об'єкта таблиці та додавання компонентів на сайт
    private void initializeContent(Class<T> type) {
        data = new ArrayList<>();
        table = new Grid<>(type, false);
        table.setMinWidth("120%");
        VerticalLayout content = new VerticalLayout();
        content.add(bar, table);
        content.setSizeFull();
        setContent(content);
    }
//потрібне якщо ви хочете додати щось на найвищому елементі сторінки
    protected void modifyNavbar(){}
}
