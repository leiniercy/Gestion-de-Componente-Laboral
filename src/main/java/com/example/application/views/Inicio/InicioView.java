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
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
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

        VerticalLayout verticalLayout = new VerticalLayout(ImagenFacultad(), ImagenWithDescription(), PostDeContactos(), Links(), footer());
        verticalLayout.setWidthFull();
        verticalLayout.setHeightFull();

        add(verticalLayout);

    }

    private Component ImagenFacultad() {

        Image img = new Image("images/F4.png", "Facultad 4");
        img.setWidthFull();
        img.setHeightFull();

        HorizontalLayout layout = new HorizontalLayout(img);
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        layout.setHeightFull();
        layout.setWidthFull();
        return layout;
    }

    private Component ImagenWithDescription() {

        HorizontalLayout title = new HorizontalLayout(
                new H1("SISTEMA DE GESTIÓN DE COMPONENTE LABORAL")
        );
        title.addClassNames("flex", "flex-col", "h-full");
        title.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        title.setWidthFull();

        Image img = new Image("images/evaluacion.png", "evaluación");
        img.setWidth("40%");
        img.setHeightFull();

        H3 h3 = new H3("Plataforma eduacional destinada al control del componente laboral"
                + " de los estudiantes repitentes de la facultad 4."
                + " El componente laboral constituye un elemento esencial en la formación inicial"
                + " de los profesionales, se expresa en forma de práctica profesional integral, implica "
                + "no solo observar y ejecutar alternativas de solución, sino reconstruir y reformular "
                + "conocimientos para poder transformar la realidad existente en su contexto de actuación, "
                + "retribuyendo a través de este a las necesidades la facultad y la univerisdad."
        );
        h3.setWidth("60%");
        h3.setHeightFull();

        HorizontalLayout contenedorDescription = new HorizontalLayout(img, h3);
        contenedorDescription.setWidthFull();
        contenedorDescription.setHeightFull();

        VerticalLayout layout = new VerticalLayout(title, contenedorDescription);
        layout.addClassNames("flex", "flex-col", "h-full");
        layout.setMaxWidth("100%");
        layout.setWidthFull();

        return layout;
    }

    private Component Links() {

        HorizontalLayout layout = new HorizontalLayout();

        H2 sitiosInteres = new H2("SITIOS DE INTERÉS");
        HorizontalLayout link1 = new HorizontalLayout(VaadinIcon.LINK.create(), new H3("Repositorio Institucional"));
        link1.setAlignItems(Alignment.BASELINE);
        Anchor repositorioInstitucional = new Anchor("https://repositorio.uci.cu/jspui/", link1);
        HorizontalLayout link2 = new HorizontalLayout(VaadinIcon.LINK.create(), new H3("Biblioteca"));
        link2.setAlignItems(Alignment.BASELINE);
        Anchor biblioteca = new Anchor("https://biblioteca.uci.cu/", link2);
        HorizontalLayout link3 = new HorizontalLayout(VaadinIcon.LINK.create(), new H3("Internos"));
        link3.setAlignItems(Alignment.BASELINE);
        Anchor internos = new Anchor("https://internos.uci.cu/", link3);
        VerticalLayout sitiosDeInteres = new VerticalLayout(sitiosInteres, repositorioInstitucional, biblioteca, internos);

        H2 otroSitios = new H2("OTROS SITIOS");
        HorizontalLayout link4 = new HorizontalLayout(VaadinIcon.LINK.create(), new H3("Portal UCI"));
        link4.setAlignItems(Alignment.BASELINE);
        Anchor portalUCI = new Anchor("https://www.uci.cu/", link4);
        HorizontalLayout link5 = new HorizontalLayout(VaadinIcon.LINK.create(), new H3("Intranet"));
        link5.setAlignItems(Alignment.BASELINE);
        Anchor intranet = new Anchor("https://intranet.uci.cu/", link5);
        HorizontalLayout link6 = new HorizontalLayout(VaadinIcon.LINK.create(), new H3("Periódico Mella"));
        link6.setAlignItems(Alignment.BASELINE);
        Anchor periodicoMella = new Anchor("https://periodico.uci.cu/", link6);
        VerticalLayout otrosSitios = new VerticalLayout(otroSitios, portalUCI, intranet, periodicoMella);

        H2 comunidadessUCI = new H2("COMUNIDADES UCI");
        HorizontalLayout link7 = new HorizontalLayout(VaadinIcon.LINK.create(), new H3("HumanOS"));
        link7.setAlignItems(Alignment.BASELINE);
        Anchor humanos = new Anchor("https://humanos.uci.cu/", link7);
        HorizontalLayout link8 = new HorizontalLayout(VaadinIcon.LINK.create(), new H3("FirefoxManía"));
        link8.setAlignItems(Alignment.BASELINE);
        Anchor fireFoxMania = new Anchor("https://firefoxmania.uci.cu/", link8);
        HorizontalLayout link9 = new HorizontalLayout(VaadinIcon.LINK.create(), new H3("iBlog"));
        link9.setAlignItems(Alignment.BASELINE);
        Anchor blog = new Anchor("https://iblog.uci.cu/", link9);
        VerticalLayout comunidadesUCI = new VerticalLayout(comunidadessUCI, humanos, fireFoxMania, blog);

        layout.add(sitiosDeInteres, otrosSitios, comunidadesUCI);
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        layout.addClassNames("bg-base");
        layout.setWidthFull();

        return layout;
    }

    private Component PostDeContactos() {

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
        
        HorizontalLayout title = new HorizontalLayout(new H3("INFORMACIÓN DE LOS DESAROLLADORES"));
        title.addClassNames("flex", "flex-col", "h-full");
        title.setAlignItems(Alignment.START);
        
        HorizontalLayout layout = new HorizontalLayout(post1, post2, post3);
        layout.setWidthFull();

        VerticalLayout verticalLayout = new VerticalLayout(title, layout);
        verticalLayout.addClassNames("flex", "flex-col", "h-full");
        verticalLayout.setWidthFull();
        verticalLayout.setHeightFull();

        return verticalLayout;
    }

    private Footer footer() {

        HorizontalLayout ly = new HorizontalLayout(new Span(VaadinIcon.ACADEMY_CAP.create()), new H3("Universidad de Ciencias Informáticas"));
        ly.setAlignItems(Alignment.BASELINE);

        Footer myFooter = new Footer(ly);
        myFooter.getStyle().set("padding", "var(--lumo-space-wide-m)");

        return myFooter;
    }

}
