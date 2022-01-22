/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.estudiante;

import com.example.application.data.ValidationMessage;
import com.example.application.data.entity.Area;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Grupo;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

/**
 *
 * @author Leinier
 */
public class EstudianteForm extends FormLayout {

    private Estudiante estudiante;

    private TextField nombre = new TextField("Nombre");
    private TextField apellidos = new TextField("Apellidos");
    private ComboBox<User> user = new ComboBox<>("Usuario");
    private EmailField email = new EmailField("Correo");
    private TextField solapin = new TextField("Solapin");
    private IntegerField anno_repitencia = new IntegerField("Año de repitencia");
    private IntegerField cantidad_asignaturas = new IntegerField("Cantidad de asignaturas");
    private ComboBox<Area> area = new ComboBox<>("Area");
    private ComboBox<Grupo> grupo = new ComboBox<>("Grupo");

    private Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    private Button close = new Button("Cancelar", VaadinIcon.ERASER.create());


    private BeanValidationBinder<Estudiante> binder = new BeanValidationBinder<>(Estudiante.class);

    public EstudianteForm(List<User> users, List<Area> areas, List<Grupo> grupos) {
        addClassName("estudiante-form");

        ValidationMessage nombreValidationMessage = new ValidationMessage();
        ValidationMessage apellidosValidationMessage = new ValidationMessage();
        ValidationMessage userValidationMessage = new ValidationMessage();
        ValidationMessage emailValidationMessage = new ValidationMessage();
        ValidationMessage solapinValidationMessage = new ValidationMessage();
        ValidationMessage annoRepitenciaValidationMessage = new ValidationMessage();
        ValidationMessage cantidadAsignaturasValidationMessage = new ValidationMessage();
        ValidationMessage areaValidationMessage = new ValidationMessage();
        ValidationMessage grupoValidationMessage = new ValidationMessage();

        binder.bindInstanceFields(this);
        binder.forField(nombre)
                .asRequired("El campo nombre no debe estar vacío")
                .withStatusLabel(nombreValidationMessage)
                .bind(Estudiante::getNombre, Estudiante::setNombre);
        binder.forField(apellidos)
                .asRequired("El campo apellidos no debe estar vacío")
                .withStatusLabel(apellidosValidationMessage)
                .bind(Estudiante::getApellidos, Estudiante::setApellidos);
        binder.forField(user)
                .asRequired("Debe seleccionar un usuario")
                .withStatusLabel(userValidationMessage)
                .bind(Estudiante::getUser, Estudiante::setUser);
        binder.forField(email)
                .asRequired("El campo correo no debe estar vacío")
                .withStatusLabel(emailValidationMessage)
                .bind(Estudiante::getEmail, Estudiante::setEmail);
        binder.forField(solapin)
                .asRequired("El campo solapín no debe estar vacío")
                .withStatusLabel(solapinValidationMessage)
                .bind(Estudiante::getSolapin, Estudiante::setSolapin);
        binder.forField(anno_repitencia)
                .asRequired("El campo no debe ser vacío")
                .withStatusLabel(annoRepitenciaValidationMessage)
                .bind(Estudiante::getAnno_repitencia, Estudiante::setAnno_repitencia);
        binder.forField(cantidad_asignaturas)
                .asRequired("El campo no debe ser vacío")
                .withStatusLabel(cantidadAsignaturasValidationMessage)
                .bind(Estudiante::getCantidad_asignaturas , Estudiante::setCantidad_asignaturas);
        binder.forField(area)
                .asRequired("Debe seleccionar un área")
                .withStatusLabel(areaValidationMessage)
                .bind(Estudiante::getArea , Estudiante::setArea);
        binder.forField(grupo)
                .asRequired("Debe seleccionar un grupo")
                .withStatusLabel(grupoValidationMessage)
                .bind(Estudiante::getGrupo , Estudiante::setGrupo);

        //Config form
        
        //nombre
        //apellidos
        //usuario
        user.setItems(users);
        user.setItemLabelGenerator(User::getName);
        //email
        email.setPlaceholder("usuario@estudiantes.uci.cu");
        email.setPattern("^.+@estudiantes.uci\\.cu$");
        email.setErrorMessage("Por favor escriba un correo válido");
        email.setClearButtonVisible(true);
        //solapin
        //grupo
        //anno_repitencia
        anno_repitencia.setHelperText("Máximo 5");
        anno_repitencia.setValue(1);
        anno_repitencia.setHasControls(true);
        anno_repitencia.setMin(1);
        anno_repitencia.setMax(5);
        //cantidad_asignaturas
        cantidad_asignaturas.setHelperText("Máximo 16");
        cantidad_asignaturas.setValue(2);
        cantidad_asignaturas.setHasControls(true);
        cantidad_asignaturas.setMin(2);
        cantidad_asignaturas.setMax(16);
        //grupo
        grupo.setItems(grupos);
        grupo.setItemLabelGenerator(Grupo::getNumero);
        //area
        area.setItems(areas);
        area.setItemLabelGenerator(Area::getNombre);

        add(
                nombre,
                apellidos,
                user,
                email,
                solapin,
                anno_repitencia,
                cantidad_asignaturas,
                area,
                grupo,
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

        return new HorizontalLayout(save,close);
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        binder.readBean(estudiante);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(estudiante);
            fireEvent(new SaveEvent(this, estudiante));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class EstudianteFormEvent extends ComponentEvent<EstudianteForm> {

        private Estudiante estudiante;

        protected EstudianteFormEvent(EstudianteForm source, Estudiante estudiante) {
            super(source, false);
            this.estudiante = estudiante;
        }

        public Estudiante getEstudiante() {
            return estudiante;
        }
    }

    public static class SaveEvent extends EstudianteFormEvent {

        SaveEvent(EstudianteForm source, Estudiante estudiante) {
            super(source, estudiante);
        }
    }

    public static class DeleteEvent extends EstudianteFormEvent {

        DeleteEvent(EstudianteForm source, Estudiante estudiante) {
            super(source, estudiante);
        }

    }

    public static class CloseEvent extends EstudianteFormEvent {

        CloseEvent(EstudianteForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
            ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
