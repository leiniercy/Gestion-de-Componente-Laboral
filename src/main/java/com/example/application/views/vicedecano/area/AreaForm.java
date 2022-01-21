/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.area;

import com.example.application.data.entity.Area;
import com.example.application.data.entity.Profesor;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

/**
 *
 * @author Leinier
 */
public class AreaForm extends FormLayout{
    
    private Area area;

    private TextField nombre = new TextField("Nombre");
    private TextField descripcion  = new TextField("Descripcion");
    
    private Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    private Button close = new Button("Cancelar", VaadinIcon.ERASER.create());
    private Button delete = new Button("Eliminar", VaadinIcon.REFRESH.create());

    private BeanValidationBinder<Area> binder = new BeanValidationBinder<>(Area.class);

    public AreaForm() {
        
        addClassName("area-form");
        binder.bindInstanceFields(this);
        binder.forField(nombre).asRequired().bind(Area::getNombre, Area::setNombre);
    

        //Config form
        
        //nombre
        //descripcion
      

        add(
                nombre,
                descripcion,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        
        save.addClickListener(event -> validateAndSave());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
      
        
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, area)));
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickShortcut(Key.DELETE);
        
        
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickShortcut(Key.ESCAPE);
        
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setArea(Area area) {
        this.area = area;
        binder.readBean(area);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(area);
            fireEvent(new SaveEvent(this, area));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class AreaFormEvent extends ComponentEvent<AreaForm> {

        private Area area;

        protected AreaFormEvent(AreaForm source, Area area) {
            super(source, false);
            this.area = area;
        }

        public Area getArea() {
            return area;
        }
    }

    public static class SaveEvent extends AreaFormEvent {

        SaveEvent(AreaForm source, Area area) {
            super(source, area);
        }
    }

    public static class DeleteEvent extends AreaFormEvent {

        DeleteEvent(AreaForm source, Area area) {
            super(source, area);
        }

    }

    public static class CloseEvent extends AreaFormEvent {

        CloseEvent(AreaForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
            ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    
    
}
