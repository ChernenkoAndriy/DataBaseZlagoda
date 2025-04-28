package com.example.demo.views.events;

import com.example.demo.repositories.database_entities.IEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class AbstractEvent<SOURCE extends Component, ENTITY extends IEntity> extends ComponentEvent<SOURCE> {
    private ENTITY e;
    public AbstractEvent(SOURCE source, ENTITY e, boolean fromClient) {
        super(source, fromClient);
        this.e=e;
    }

    public ENTITY getEntity() {
        return e;
    }
}
