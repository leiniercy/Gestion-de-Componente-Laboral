/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.area;

import com.example.application.data.entity.Area;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;


/**
 * @author Leinier
 */
public class AreaForm extends FormLayout {

    private Area area;

    private TextField nombre = new TextField();
    private TextArea descripcion = new TextArea();

    private Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    private Button close = new Button("Cancelar", VaadinIcon.REFRESH.create());

    private BeanValidationBinder<Area> binder = new BeanValidationBinder<>(Area.class);

    public AreaForm() {

        addClassName("area-form");

        binder.bindInstanceFields(this);

        //nombre
        nombre.setLabel("Nombre");
        nombre.getElement().setAttribute("nombre", "Ejemplo: Mi Área");
        nombre.setAutofocus(true);
        nombre.setRequired(true);
        nombre.setMinLength(2);
        nombre.setMaxLength(50);
        nombre.setPattern("^[a-zA-Z][a-zA-Z\\s]+$");
        nombre.setErrorMessage("Solo letras, mínimo 2 caracteres y máximo 50");
        nombre.addValueChangeListener(event -> {
            event.getSource().setHelperText(event.getValue().length() + "/" + 50);
        });

        /*
        nombre.setPlaceholder("Nombre de área...");
        nombre.focus();
        nombre.setValue("Mi Área");
        nombre.setAutoselect(true);
        nombre.setClearButtonVisible(true);
        nombre.addValueChangeListener(event -> {
            if (nombre.isInvalid()) {
                Notification.show("Nombre Incorrecto ");
            }
        });
        */

        //descripcion
        descripcion.setLabel("Descripión");
        descripcion.setWidthFull();
        descripcion.setMinLength(3);
        descripcion.setMaxLength(255);
        descripcion.setMinHeight("100px");
        descripcion.setMaxHeight("150px");
        //descripcion.setPattern("^[a-zA-Z0-9][a-zA-Z0-9\\s]*$");
        descripcion.setErrorMessage("Solo caracteres y numeros, mínimo 3 caracteres y  máximo 255");
        descripcion.setValueChangeMode(ValueChangeMode.EAGER);
        descripcion.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 255);
        });
        add(
                nombre,
                descripcion,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {

        save.addClickListener(event -> validateAndSave());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    public void setArea(Area area) {
        this.area = area;
        binder.readBean(area);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(area);
            fireEvent(new SaveEvent(this, area));
            Notification.show("Área añadida");
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
