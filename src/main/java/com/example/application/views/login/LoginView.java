/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 *
 * @author Leinier
 */
@Route("login")
@PageTitle("Login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

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

        i18n.getErrorMessage().setTitle("Error:");
        i18n.getErrorMessage().setMessage("Usuario o contraseña incorrectos");

        setI18n(i18n);
        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            setError(true);
        }
    }

}
