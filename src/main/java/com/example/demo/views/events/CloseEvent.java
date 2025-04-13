package com.example.demo.views.events;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class CloseEvent<SOURCE extends Component> extends ComponentEvent<SOURCE> {
    public CloseEvent(SOURCE source) {
        super(source, false);
    }
}
