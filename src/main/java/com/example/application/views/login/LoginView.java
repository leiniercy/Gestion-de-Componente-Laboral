/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.login;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Collections;

/**
 *
 * @author Leinier
 */
@Route("login")
@PageTitle("Login")
public class LoginView extends Composite<LoginOverlay> /*implements BeforeEnterListener*/ {

    public LoginView() {
        getContent().setOpened(true);
        getContent().setTitle("GCL");
        getContent().setDescription("Gestión Componente Laboral");
        getContent().setAction("login");
    }
}
