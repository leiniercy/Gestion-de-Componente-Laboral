package com.example.application.views.vicedecano;

import com.example.application.data.service.DataService;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Evaluacion;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.themes.LumoDarkTheme;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "dashboard", layout = MainLayout.class)
@RolesAllowed("vicedecano")
public class DashboardView extends VerticalLayout {

    DataService service;

    public DashboardView(@Autowired DataService service) {
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(getBoard(), getGraficosPasteles());
    }


    private Component getBoard() {
        addClassName("basic-board");
        Board board = new Board();

        board.addRow(
                createHighlight("", new Span()),
                createHighlight("Estudiantes", (Span) getEstudiantes()),
                createHighlight("Profesores", (Span) getProfesores()),
                createHighlight("", new Span())
        );
        return board;
    }

    private Component createHighlight(String title, Span span) {

        H1 h1 = new H1(title);
        h1.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");

        span.addClassNames("font-semibold", "text-3xl");

        VerticalLayout layout = new VerticalLayout(h1, span /*, badge*/);
        layout.addClassName("p-l");
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getElement().getThemeList().add("spacing-l");
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private Component getEstudiantes() {
        Span stats = new Span(service.countEstudiante() + "");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }

    private Component getProfesores() {
        Span stats = new Span(service.countProfesor() + "");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }

    //Graficos de pasteles
    private Component getGraficosPasteles() {
        addClassName("grafics_key-board");
        Board board = new Board();
        board.addRow(
                getEstudiante_Bien_Area(),
                getEstudiante_Regular_Area(),
                getEstudiante_Mal_Area()
        );

        return board;
    }

    private HorizontalLayout createHeader(String title) {
        H2 h2 = new H2(title);
        h2.addClassNames("text-xl", "m-0");

        VerticalLayout column = new VerticalLayout(h2);
        column.setPadding(false);
        column.setSpacing(false);

        HorizontalLayout header = new HorizontalLayout(column);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setSpacing(false);
        header.setWidthFull();
        return header;
    }


    private Component getEstudiante_Mal_Area() {

        HorizontalLayout header = createHeader("Total de tareas evaluadas de Mal por Área");
        Chart chart = new Chart(ChartType.PIE);

        List<Evaluacion> list = service.findAllEvaluacion();

        DataSeries dataSeries = new DataSeries();
        service.findAllArea().forEach(
                area -> dataSeries.add(
                        new DataSeriesItem(String.format(
                                area.getNombre() + " %d"
                                , list.stream().filter(evaluacion -> "M".equals(evaluacion.getNota()) && evaluacion.getEstudiante().getArea().getNombre().equals(area.getNombre())).count()
                        )
                                , service.countArea()
                        )
                )
        );
        chart.getConfiguration().setSeries(dataSeries);

        long malCount = list.stream()
                .filter(evaluacion -> "M".equals(evaluacion.getNota()))
                .count();

        Span stats = new Span(String.format("%d", malCount));
        stats.addClassNames("text-xl", "mt-m");

        VerticalLayout serviceHealth = new VerticalLayout(header, stats, chart);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        serviceHealth.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return serviceHealth;
    }

    private Component getEstudiante_Regular_Area() {

        HorizontalLayout header = createHeader("Total de tareas evaluadas de Regular por Área");
        Chart chart = new Chart(ChartType.PIE);
        List<Evaluacion> list = service.findAllEvaluacion();

        DataSeries dataSeries = new DataSeries();
        service.findAllArea().forEach(
                area -> dataSeries.add(
                        new DataSeriesItem(String.format(
                                area.getNombre() + " %d"
                                , list.stream().filter(evaluacion -> "R".equals(evaluacion.getNota()) && evaluacion.getEstudiante().getArea().getNombre().equals(area.getNombre())).count()
                        )
                                , service.countArea()
                        )
                )
        );
        chart.getConfiguration().setSeries(dataSeries);

        long regularCount = list.stream()
                .filter(evaluacion -> "R".equals(evaluacion.getNota()))
                .count();

        Span stats = new Span(String.format("%d", regularCount));
        stats.addClassNames("text-xl", "mt-m");

        VerticalLayout serviceHealth = new VerticalLayout(header, stats, chart);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        serviceHealth.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return serviceHealth;
    }

    private Component getEstudiante_Bien_Area() {

        HorizontalLayout header = createHeader("Total de tareas evaluadas de Bien por Área");
        Chart chart = new Chart(ChartType.PIE);
        List<Evaluacion> list = service.findAllEvaluacion();

        DataSeries dataSeries = new DataSeries();
        service.findAllArea().forEach(
                area -> dataSeries.add(
                        new DataSeriesItem(String.format(
                                area.getNombre() + " %d"
                                , list.stream().filter(evaluacion -> "B".equals(evaluacion.getNota()) && evaluacion.getEstudiante().getArea().getNombre().equals(area.getNombre())).count()
                        )
                                , service.countArea()
                        )
                )
        );
        chart.getConfiguration().setSeries(dataSeries);

        long bienCount = list.stream()
                .filter(evaluacion -> "B".equals(evaluacion.getNota()))
                .count();

        Span stats = new Span(String.format("%d", bienCount));
        stats.addClassNames("text-xl", "mt-m");

        VerticalLayout serviceHealth = new VerticalLayout(header, stats, chart);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        serviceHealth.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return serviceHealth;
    }

}
