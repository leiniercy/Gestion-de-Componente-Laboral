/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.Inicio;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
@JsModule("./views/imagelist/image-list-view.ts")
@AnonymousAllowed
public class InicioView extends VerticalLayout {

    
    public InicioView() {

        addClassNames("image-list-view", "flex", "flex-col", "h-full");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        Image img = new Image("images/Fac4.png", "placeholder plant");
        img.setWidthFull();
        img.setHeightFull();
        
        
        HorizontalLayout ly = new HorizontalLayout(new Span(VaadinIcon.ACADEMY_CAP.create()),new H6("Universidad de Ciencias Informáticas") );
        ly.setAlignItems(Alignment.BASELINE);
        
        Footer footer = new Footer(ly);
        footer.getStyle().set("padding", "var(--lumo-space-wide-m)");
        
        add(img, PostDeContactos(), footer);

    }
    
    
    private Component PostDeContactos(){
        
         ImageListViewCard post1 = new ImageListViewCard(
                "leiniercy",
                "images/leinier.png",
                "Contacto",
                "Nombre: Leinier Caraballo Yanes",
                "Móvil: +53 58503871"
        );
        
        ImageListViewCard post2 = new ImageListViewCard(
                "christiansj",
                "images/christian.png",
                "Contacto",
                "Nombre: Christian Sosa Jiménez",
                "Móvil: +53 54571775"
        );
        
        ImageListViewCard post3 = new ImageListViewCard(
                "felixacg",
                "images/andry.png",
                "Contacto",
                "Nombre: Félix Andrés Corría Gutiérrez",
                "Móvil: +53 55360989"
        );
        HorizontalLayout layout =  new HorizontalLayout(post1,post2,post3);
        layout.setAlignItems(Alignment.CENTER);
        
        return layout;
    }
    
   
}
