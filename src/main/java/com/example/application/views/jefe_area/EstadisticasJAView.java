package com.example.application.views.jefe_area;

import com.example.application.data.entity.Evaluacion;
import com.example.application.views.vicedecano.*;
import com.example.application.data.service.*;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    public EstadisticasJAView(
            @Autowired UserService userService,
            @Autowired AreaService areaService,
            @Autowired EstudianteService estudianteService,
            @Autowired ProfesorService profesorService,
            @Autowired EvaluacionService evaluacionService,
            @Autowired GrupoService grupoService,
            @Autowired TareaService tareaService
        ) {
        
        this.userService = userService;
        this.areaService = areaService;
        this.estudianteService = estudianteService;
        this.profesorService = profesorService;
        this.evaluacionService = evaluacionService;
        this.evaluacionService = evaluacionService;
        this.tareaService = tareaService;
        
        addClassName("estadistica-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(getBoard(), createViewEvents(), getGraficosPasteles());
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
        Span stats = new Span(estudianteService.countEstudiante() + "");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }


    private Component createViewEvents() {
        // Header
        Select year = new Select();
        year.setItems("2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021");
        year.setValue("2021");
        year.setWidth("100px");

        HorizontalLayout header = createHeader("Gráfica de evaluaciones: ");
        header.add(year);

        // Chart
        Chart chart = new Chart(ChartType.AREA);
        Configuration conf = chart.getConfiguration();

        XAxis xAxis = new XAxis();
        xAxis.setCategories("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiempbre", "Octubre", "Noviembre", "Diciembre");
        conf.addxAxis(xAxis);

        conf.getyAxis().setTitle("Values");

        PlotOptionsArea plotOptions = new PlotOptionsArea();
        plotOptions.setPointPlacement(PointPlacement.ON);
        conf.addPlotOptions(plotOptions);

        conf.addSeries(new ListSeries("Berlin", 189, 191, 191, 196, 201, 203, 209, 212, 229, 242, 244, 247));
        conf.addSeries(new ListSeries("London", 138, 146, 148, 148, 152, 153, 163, 173, 178, 179, 185, 187));
        conf.addSeries(new ListSeries("New York", 65, 65, 66, 71, 93, 102, 108, 117, 127, 129, 135, 136));
        conf.addSeries(new ListSeries("Tokyo", 0, 11, 17, 23, 30, 42, 48, 49, 52, 54, 58, 62));

        // Add it all together
        VerticalLayout viewEvents = new VerticalLayout(header, chart);
        viewEvents.addClassName("p-l");
        viewEvents.setPadding(false);
        viewEvents.setSpacing(false);
        viewEvents.getElement().getThemeList().add("spacing-l");
        return viewEvents;
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
        HorizontalLayout header = createHeader("Total de tareas evaluadas de mal por área ");
        Chart chart = new Chart(ChartType.PIE);
        List<Evaluacion> list = evaluacionService.findAllEvaluacion();

        long mal = list.stream()
                .filter(evaluacion -> "M".equals(evaluacion.getNota()))
                .count();

        DataSeries dataSeries = new DataSeries();
        areaService.findAllArea().forEach(area
                        -> dataSeries.add(new DataSeriesItem(String.format(
                        area.getNombre() + " %d", list.stream().filter(evaluacion -> "M".equals(evaluacion.getNota()) && evaluacion.getEstudiante().getArea().getNombre().equals(area.getNombre())).count()
                ), areaService.countArea()))
        );

        chart.getConfiguration().setSeries(dataSeries);

        Span stats = new Span(String.format("%d", mal));
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

        HorizontalLayout header = createHeader("Total de tareas evaluadas de regular por  área");
        Chart chart = new Chart(ChartType.PIE);
        List<Evaluacion> list = evaluacionService.findAllEvaluacion();

        long regular = list.stream()
                .filter(evaluacion -> "R".equals(evaluacion.getNota()))
                .count();

        DataSeries dataSeries = new DataSeries();
        areaService.findAllArea().forEach(area
                -> dataSeries.add(
                new DataSeriesItem(
                        String.format(area.getNombre() + " %d", list.stream().filter(evaluacion -> "R".equals(evaluacion.getNota()) && evaluacion.getEstudiante().getArea().getNombre().equals(area.getNombre())).count()
                        ), areaService.countArea())
                )
        );
        chart.getConfiguration().setSeries(dataSeries);

        Span stats = new Span(String.format("%d", regular));
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

        HorizontalLayout header = createHeader("Total de tareas evaluadas de bien por área");
        Chart chart = new Chart(ChartType.PIE);
        List<Evaluacion> list = evaluacionService.findAllEvaluacion();

        long bien = list.stream()
                .filter(evaluacion -> "B".equals(evaluacion.getNota()))
                .count();

        DataSeries dataSeries = new DataSeries();
        areaService.findAllArea().forEach(area
                        -> dataSeries.add(
                        new DataSeriesItem(
                                String.format(area.getNombre() + " %d", list.stream().filter(evaluacion -> "B".equals(evaluacion.getNota()) && evaluacion.getEstudiante().getArea().getNombre().equals(area.getNombre())).count()
                                ), areaService.countArea())
                )
        );
        chart.getConfiguration().setSeries(dataSeries);

        Span stats = new Span(String.format("%d",bien));
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
