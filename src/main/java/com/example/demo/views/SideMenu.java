package com.example.demo.views;

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
//ось тут аргументи path в конструкторі це назви route майбутніх сторінок. коли створюєте сторінку вибирайте
// один з вільних route тут. Інакше бокова менюшка перенаправить вас не на сторінку яку ви хотіли а впусте місце.
// Що таке route і навіщо він читати в MainView
public class SideMenu extends SideNav {

    public SideMenu() {
       addItem(new SideNavItem("Employees", "manager"));
       addItem(new SideNavItem("Customers", "customers"));
       addItem(new SideNavItem("Warehouse content", "warehouse"));
       addItem(new SideNavItem("Product Categories", "categories"));
       addItem(new SideNavItem("Product Items", "product_tems"));
       addItem(new SideNavItem("Checks", "checks"));
    }
}
