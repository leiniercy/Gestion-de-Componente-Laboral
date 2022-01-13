/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.Inicio;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 *
 * @author Leinier
 */

@PageTitle("Inicio")
@Route(value = "inicio", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class InicioView extends VerticalLayout {

    public InicioView() {
    H1 title = new H1("Pantalla de Inicio");
    add(title);
    }
    
    
}
