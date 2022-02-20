package com.example.application.views.jefe_area;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.util.LinkedList;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PageTitle("Estádisticas")
@Route(value = "estadistica", layout = MainLayout.class)
@RouteAlias(value = "estadistica", layout = MainLayout.class)
@RolesAllowed("jefeArea")
public class EstadisticasJAView extends VerticalLayout {

    private UserService userService;
    private AreaService areaService;
    private EstudianteService estudianteService;
    private ProfesorService profesorService;
    private EvaluacionService evaluacionService;
    private GrupoService grupoService;
    private TareaService tareaService;

    private List<Evaluacion> listaEvaluaciones;

    private List<Estudiante> listaEstudiantes;

    private List<Tarea> listaTareas;

    private Profesor profesor_registrado;

    private User user;

    private List<Profesor> profesores;

    private AuthenticatedUser authenticatedUser;

    public EstadisticasJAView(
            @Autowired AuthenticatedUser authenticatedUser,
            @Autowired UserService userService,
            @Autowired AreaService areaService,
            @Autowired EstudianteService estudianteService,
            @Autowired ProfesorService profesorService,
            @Autowired EvaluacionService evaluacionService,
            @Autowired GrupoService grupoService,
            @Autowired TareaService tareaService
    ) {

        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
        this.areaService = areaService;
        this.estudianteService = estudianteService;
        this.profesorService = profesorService;
        this.evaluacionService = evaluacionService;
        this.evaluacionService = evaluacionService;
        this.tareaService = tareaService;

        addClassName("estadistica-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {

            profesores = profesorService.findAllProfesor();

            user = maybeUser.get();
            Optional<Profesor> profesor = profesores.stream().filter(pro -> pro.getUser().getUsername().equals(user.getUsername())).findFirst();
            profesor_registrado = profesor.get();

            LlenarListas();

            if (listaEvaluaciones.size() != 0) {

                HorizontalLayout ly = new HorizontalLayout(new Span(VaadinIcon.ACADEMY_CAP.create()), new H6("Universidad de Ciencias Informáticas"));
                ly.setAlignItems(Alignment.BASELINE);
                Footer footer = new Footer(ly);
                footer.getStyle().set("padding", "var(--lumo-space-wide-m)");

                add(getBoard(), getGraficosPasteles(), footer);

            } else {
                add(new H1("No hay evaluaciones disponibles"));
            }

        } else {
            add(new H1("Hola Mundo"));
        }

    }

    private void LlenarListas() {

        listaEvaluaciones = evaluacionService.findAllEvaluacion();

        listaEvaluaciones = listaEvaluaciones.stream()
                .filter(eva -> eva.getEstudiante().getArea().getNombre().equals(profesor_registrado.getA().getNombre()))
                .collect(Collectors.toList());

        listaEstudiantes = estudianteService.findAllEstudiante();

        listaEstudiantes = listaEstudiantes.stream()
                .filter(est -> est.getArea().getNombre().equals(profesor_registrado.getA().getNombre()))
                .collect(Collectors.toList());

        listaTareas = tareaService.findAllTareas();

        listaTareas = listaTareas.stream()
                .filter(tarea -> tarea.getE().getArea().getNombre().equals(profesor_registrado.getA().getNombre()))
                .collect(Collectors.toList());

    }

    private Component getBoard() {
        addClassName("basic-board");
        Board board = new Board();

        board.addRow(
                createHighlight("", new Span()),
                createHighlight("Estudiantes", (Span) getEstudiantes()),
                createHighlight("", new Span())
        );
        return board;
    }

    private Component createHighlight(String title, Span span) {

        H1 h1 = new H1(title);
        h1.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");

        span.addClassNames("font-semibold", "text-3xl");

        VerticalLayout layout = new VerticalLayout(h1, span);
        layout.addClassName("p-l");
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getElement().getThemeList().add("spacing-l");
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private Component getEstudiantes() {
        Span stats = new Span(String.format("%d", listaEstudiantes.size()));
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }

    //Graficos de pasteles
    private Component getGraficosPasteles() {
        addClassName("grafics_key-board");
        Board board = new Board();
        board.addRow(
                getEvaluaciones()
        );

        return board;
    }

    private Component getEvaluaciones() {

        Chart chart = new Chart(ChartType.PIE);

        long bien = listaEvaluaciones.stream()
                .filter(evaluacion -> "B".equals(evaluacion.getNota())
                && evaluacion.getEstudiante().getArea().getNombre().equals(profesor_registrado.getA().getNombre()))
                .count();

        long regular = listaEvaluaciones.stream()
                .filter(evaluacion -> "R".equals(evaluacion.getNota())
                && evaluacion.getEstudiante().getArea().getNombre().equals(profesor_registrado.getA().getNombre())
                )
                .count();

        long mal = listaEvaluaciones.stream()
                .filter(evaluacion -> "M".equals(evaluacion.getNota())
                && evaluacion.getEstudiante().getArea().getNombre().equals(profesor_registrado.getA().getNombre())
                )
                .count();

        float promedioB = bien * 100 / (bien + regular + mal);
        float promedioR = regular * 100 / (bien + regular + mal);
        float promedioM = mal * 100 / (bien + regular + mal);

        DataSeries dataSeries = new DataSeries();
        dataSeries.add(new DataSeriesItem(String.format("Bien: %d", bien), promedioB));
        dataSeries.add(new DataSeriesItem(String.format("Regular: %d", regular), promedioR));
        dataSeries.add(new DataSeriesItem(String.format("Mal: %d", mal), promedioM));

        chart.getConfiguration().setSeries(dataSeries);
        chart.getConfiguration().setTitle("Resumen de evaluaciones");
        chart.getConfiguration().setSubTitle(String.format("Total evaluaciones: %d", (bien + regular + mal)));
        chart.addClassNames("text-xl", "mt-m");

        VerticalLayout serviceHealth = new VerticalLayout(chart);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        serviceHealth.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return serviceHealth;
    }

}
