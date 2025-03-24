package com.example.demo;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
//ця сторінка буде першою що побачить касир після реєстрації
//поки в ній тільки сміття коли я грався з компонентами, її переробимо коли буде план
@Route("cashier")
public class CashierInitialView extends AppLayout {
    public CashierInitialView() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Zlagoda");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        Button logOut = new Button("Log Out");
        logOut.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Div spacer = new Div();
        spacer.setHeight("30px");
        spacer.setWidth("85%");
        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title, spacer, logOut);
    }

    private SideNav getSideNav() {
        SideNav sideNav = new SideNav();

        sideNav.addItem(new SideNavItem("Goods"));
        sideNav.addItem(new SideNavItem("Warehouse content"));
        sideNav.addItem(new SideNavItem("Clients information"));
        sideNav.addItem(new SideNavItem(""));
        return sideNav;
    }
}
