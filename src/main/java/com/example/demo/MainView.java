package com.example.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("") // Це буде головна сторінка
public class MainView extends VerticalLayout {
    public MainView() {
        Button button = new Button("Натисни мене", event ->
                Notification.show("Привіт із Vaadin!")
        );
        add(button);
    }
}
