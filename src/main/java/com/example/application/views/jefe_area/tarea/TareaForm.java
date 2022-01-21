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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import java.util.List;

/**
 *
 * @author Leinier
 */
public class TareaForm extends FormLayout {
    
    
     private Tarea tarea;

    private TextField nombre = new TextField("Nombre");
    private TextField descripcion  = new TextField("Descripción");
     private DatePicker fecha_inicio = new DatePicker("Fecha de Inicio");
    private DatePicker fecha_fin = new DatePicker("Fecha de fin");
    private ComboBox<Estudiante> e = new ComboBox<>("Estudiante");
    
    private Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    private Button close = new Button("Cancelar", VaadinIcon.ERASER.create());
    private Button delete = new Button("Eliminar", VaadinIcon.REFRESH.create());

    private BeanValidationBinder<Tarea> binder = new BeanValidationBinder<>(Tarea.class);

    public TareaForm(List<Estudiante> estudiantes ) {
        
        addClassName("tarea-form");
        binder.bindInstanceFields(this);

        e.setItems(estudiantes);
        e.setItemLabelGenerator(Estudiante::getStringNombreApellidos);

        //Config form
        
        //nombre
        //descripcion
        //fecha_inicio
        //fecha_fin
        //e
      

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
      
        
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, tarea)));
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickShortcut(Key.DELETE);
        
        
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickShortcut(Key.ESCAPE);
        
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
        binder.readBean(tarea);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(tarea);
            fireEvent(new SaveEvent(this, tarea));
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
