/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.jefe_area.tarea;

import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Tarea;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 *
 * @author Leinier
 */
public class TareaForm extends FormLayout {

    private Tarea tarea;

    private TextField nombre = new TextField("Nombre");
    private TextArea descripcion = new TextArea("Descripción");
    private DatePicker fecha_inicio = new DatePicker("Fecha de Inicio");
    private DatePicker fecha_fin = new DatePicker("Fecha de fin");
    private ComboBox<Estudiante> e = new ComboBox<>("Estudiante");

    private Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    private Button close = new Button("Cancelar", VaadinIcon.REFRESH.create());

    private BeanValidationBinder<Tarea> binder = new BeanValidationBinder<>(Tarea.class);

    public TareaForm(List<Estudiante> estudiantes) {

        addClassName("tarea-form");

        binder.bindInstanceFields(this);

        //Config form
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
        //descripcion
        descripcion.setLabel("Descripión");
        descripcion.setWidthFull();
        descripcion.setMinLength(3);
        descripcion.setMaxLength(255);
        descripcion.setMinHeight("100px");
        descripcion.setMaxHeight("150px");
//        descripcion.setPattern("^[a-zA-Z][a-zA-Z0-9\\s]*$");
        descripcion.setErrorMessage("Solo caracteres y numeros, mínimo 3 caracteres y  máximo 255");
        descripcion.setValueChangeMode(ValueChangeMode.EAGER);
        descripcion.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 255);
        });
        //fecha_inicio
        fecha_inicio.setMin(LocalDate.now(ZoneId.systemDefault()));
        //fecha_fin
        fecha_fin.setMin(LocalDate.now(ZoneId.systemDefault()));
        //e
        e.setItems(estudiantes);
        e.setItemLabelGenerator(Estudiante::getStringNombreApellidos);

        add(
                nombre,
                descripcion,
                fecha_inicio,
                fecha_fin,
                e,
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

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
        binder.readBean(tarea);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(tarea);
            fireEvent(new SaveEvent(this, tarea));
            Notification.show("Tarea añadida");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class TareaFormEvent extends ComponentEvent<TareaForm> {

        private Tarea tarea;

        protected TareaFormEvent(TareaForm source, Tarea tarea) {
            super(source, false);
            this.tarea = tarea;
        }

        public Tarea getTarea() {
            return tarea;
        }
    }

    public static class SaveEvent extends TareaFormEvent {

        SaveEvent(TareaForm source, Tarea tarea) {
            super(source, tarea);
        }
    }

    public static class DeleteEvent extends TareaFormEvent {

        DeleteEvent(TareaForm source, Tarea tarea) {
            super(source, tarea);
        }

    }

    public static class CloseEvent extends TareaFormEvent {

        CloseEvent(TareaForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
            ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
