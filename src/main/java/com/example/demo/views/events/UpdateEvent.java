package com.example.demo.views.events;
import com.example.demo.views.repositories.database_entities.IEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class UpdateEvent<SOURCE extends Component> extends ComponentEvent<SOURCE> {
    public UpdateEvent(SOURCE source) {
        super(source, false);
    }}
