package com.example.application.views.dashboard;

import com.example.application.data.DataService;
import com.example.application.views.MainLayout;
import com.example.application.views.dashboard.ServiceHealth.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
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
//        VaadinIcon icon = VaadinIcon.ARROW_UP;
        String prefix = "";
        String theme = "badge";

        H1 h1 = new H1(title);
        h1.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");

        span.addClassNames("font-semibold", "text-3xl");
//        Icon i = icon.create();
//        i.addClassNames("box-border", "p-xs");

//        Span badge = new Span(i, new Span(prefix));
//        badge.getElement().getThemeList().add(theme);
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
        HorizontalLayout header = createHeader("Estudiantes evaluados de mal");

        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();
        service.findAllArea().forEach(area
                -> dataSeries.add(new DataSeriesItem(area.getNombre(), service.countArea()))
        );
        chart.getConfiguration().setSeries(dataSeries);

        Span stats = new Span(service.countEstudiante() + "");
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
        HorizontalLayout header = createHeader("Estudiantes evaluados de Regular");

        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();
        service.findAllArea().forEach(area
                -> dataSeries.add(new DataSeriesItem(area.getNombre(), service.countArea()))
        );
        chart.getConfiguration().setSeries(dataSeries);

        Span stats = new Span(service.countEstudiante() + "");
        stats.addClassNames("text-xl", "mt-m");

        VerticalLayout serviceHealth = new VerticalLayout(header,stats ,chart);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        serviceHealth.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return serviceHealth;
    }

    private Component getEstudiante_Bien_Area() {

        HorizontalLayout header = createHeader("Estudiantes evaluados de Bien");
        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();
        service.findAllArea().forEach(area
                -> dataSeries.add(new DataSeriesItem(area.getNombre(), service.countArea()))
        );
        chart.getConfiguration().setSeries(dataSeries);

        Span stats = new Span(service.countEstudiante() + "");
        stats.addClassNames("text-xl", "mt-m");

        VerticalLayout serviceHealth = new VerticalLayout(header,stats,chart);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        serviceHealth.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return serviceHealth;
    }

}
