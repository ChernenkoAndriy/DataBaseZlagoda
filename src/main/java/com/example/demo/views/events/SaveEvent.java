package com.example.demo.views.events;
import com.example.demo.repositories.database_entities.IEntity;
import com.vaadin.flow.component.Component;

public class SaveEvent<SOURCE extends Component, ENTITY extends IEntity> extends AbstractEvent<SOURCE, ENTITY> {
    public SaveEvent(SOURCE source, ENTITY e) {
        super(source, e, false);
    }}
