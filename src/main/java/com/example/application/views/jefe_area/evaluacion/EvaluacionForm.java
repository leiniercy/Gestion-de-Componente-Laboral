/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.jefe_area.evaluacion;


import com.example.application.data.ValidationMessage;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Tarea;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

/**
 *
 * @author Leinier
 */
public class EvaluacionForm extends FormLayout{
    
    private Evaluacion evaluacion;

    private TextField nota = new TextField("Nota");
    private TextArea descripcion = new TextArea("Descripcion");
    private ComboBox<Estudiante> estudiante = new ComboBox<>("Estudiante");
    private ComboBox<Tarea> tarea = new ComboBox<>("Tarea");
    private ComboBox<String> status = new ComboBox<>("Status");

    private Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    private Button close = new Button("Cancelar", VaadinIcon.REFRESH.create());

    private BeanValidationBinder<Evaluacion> binder = new BeanValidationBinder<>(Evaluacion.class);

    public EvaluacionForm() {
        
        addClassName("evaluacion-form");

        ValidationMessage notaValidationMessage = new ValidationMessage();
        ValidationMessage fecha_inicioValidationMessage = new ValidationMessage();
        ValidationMessage tareaalidationMessage = new ValidationMessage();
        ValidationMessage estudianteValidationMessage = new ValidationMessage();
        ValidationMessage statusValidationMessage = new ValidationMessage();

        binder.bindInstanceFields(this);
        binder.forField(nota)
                .asRequired("El campo nota no debe estar vacío")
                .withStatusLabel(notaValidationMessage)
                .bind(Evaluacion::getNota, Evaluacion::setNota);
        binder.forField(estudiante)
                .asRequired("Debe seleccionar un estudiante")
                .withStatusLabel(fecha_inicioValidationMessage)
                .bind(Evaluacion::getEstudiante, Evaluacion::setEstudiante);
        binder.forField(tarea)
                .asRequired("Debe seleccionar una tarea")
                .withStatusLabel(tareaalidationMessage)
                .bind(Evaluacion::getTarea , Evaluacion::setTarea);
        binder.forField(status)
                .asRequired("Debe seleccionar un Status")
                .withStatusLabel(statusValidationMessage)
                .bind(Evaluacion::getStatus , Evaluacion::setStatus);


        //Config form
        
        //nombre
        //descripcion
        //estudiante
        //tarea
        //status

        add(
                nota,
                descripcion,
                estudiante,
                tarea,
                status,
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

    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
        binder.readBean(evaluacion);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(evaluacion);
            fireEvent(new SaveEvent(this, evaluacion));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class EvaluacionFormEvent extends ComponentEvent<EvaluacionForm> {

        private Evaluacion evaluacion;

        protected EvaluacionFormEvent(EvaluacionForm source, Evaluacion evaluacion) {
            super(source, false);
            this.evaluacion = evaluacion;
        }

        public Evaluacion getEvaluacion() {
            return evaluacion;
        }
    }

    public static class SaveEvent extends EvaluacionFormEvent {

        SaveEvent(EvaluacionForm source, Evaluacion evaluacion) {
            super(source, evaluacion);
        }
    }

    public static class DeleteEvent extends EvaluacionFormEvent {

        DeleteEvent(EvaluacionForm source, Evaluacion evaluacion) {
            super(source, evaluacion);
        }

    }

    public static class CloseEvent extends EvaluacionFormEvent {

        CloseEvent(EvaluacionForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
            ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    
    
    
}
