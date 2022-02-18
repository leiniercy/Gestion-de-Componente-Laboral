/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 *
 * @author Leinier
 */
@Route("login")
@PageTitle("Login")
public class LoginView extends LoginOverlay {

    public LoginView() {
        
        setAction("login");

        LoginI18n i18n = LoginI18n.createDefault();
        
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("GCL");
        i18n.getHeader().setDescription("Gestión Componente Laboral");
        
        i18n.setAdditionalInformation(null);
        
        i18n.getForm().setTitle("");
        i18n.getForm().setUsername("Usuario:");
        i18n.getForm().setPassword("Contraseña:");
        i18n.getForm().setSubmit("Iniciar sesión");
        
        
        
        LoginI18n.ErrorMessage errorMessage = i18n.getErrorMessage();
        errorMessage.setTitle("Error de auntenticación");
        errorMessage.setMessage("Usuario o contraseña incorrectos");
        i18n.setErrorMessage(errorMessage);
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);
        
    }
}
