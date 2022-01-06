/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.pruebas;

import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Grupo;
import com.example.application.data.service.GrupoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import javax.annotation.security.RolesAllowed;
import org.vaadin.crudui.crud.impl.GridCrud;

/**
 *
 * @author Leinier
 */
@PageTitle("Prueba")
@Route("prueba")
@RolesAllowed("admin")
public class PruebasView extends Div {

    
    public PruebasView(GrupoService service) {

        var crud = new GridCrud<>(Grupo.class, service);
        crud.getGrid().setColumns("firstName", "lastName", "email", "phone", "dateOfBirth", "occupation", "important");
        crud.getCrudFormFactory().setVisibleProperties("firstName", "lastName", "email", "phone", "dateOfBirth", "occupation", "important");
//        crud.setAddOperationVisible(false);
//
//        var firstName = new TextField("firstName");
//        var lastName = new TextField("lastName");
//        var email = new TextField("email");
//        var phone = new TextField("phone");
//        var dateOfBirth = new DatePicker("dateOfBirth");
//        var occupation = new TextField("occupation");
//        var important =  new Checkbox("important");
        
        
//        var nota = new TextField("Nota");
//        var descripcion = new TextArea("descripcion");
//
//        var binder = new BeanValidationBinder<>(Evaluacion.class);
//
//        /*Button*/
//        var save = new Button("Save",e -> {
//           
//        });
//        var cancel = new Button("Reset", e ->{
//        
//        
//        
//        });
        /*Button*/
        add(crud);

    }

}
