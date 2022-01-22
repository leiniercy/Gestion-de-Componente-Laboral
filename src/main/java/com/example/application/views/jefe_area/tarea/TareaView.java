/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.jefe_area.tarea;

import com.example.application.data.DataService;
import com.example.application.data.entity.Area;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Tarea;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.LocalDate;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Leinier
 */
@PageTitle("Tarea")
@Route(value = "tarea-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("jefeArea")
public class TareaView extends VerticalLayout {

    private Grid<Tarea> grid = new Grid<>(Tarea.class, false);

    TareaForm form;

    private DataService dataService;

    private GridListDataView<Tarea> gridListDataView;

    private Grid.Column<Tarea> nombreColumn = grid.addColumn(Tarea::getNombre).setHeader("Nombre").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Tarea> descripcionColumn = grid.addColumn(Tarea::getDescripcion).setHeader("Descripción").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Tarea> fecha_inicioColumn = grid.addColumn(Tarea::getFecha_inicio).setHeader("Fecha de inicio").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Tarea> fecha_finColumn = grid.addColumn(Tarea::getFecha_fin).setHeader("Fecha de fin").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Tarea> estudianteColumn = grid.addColumn(tarea -> tarea.getE().getStringNombreApellidos()).setHeader("Estudiante").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Tarea> editColumn = grid.addComponentColumn(tarea -> {
        HorizontalLayout layout = new HorizontalLayout();
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickShortcut(Key.F2);
        editButton.addClickListener(e -> this.editTarea(tarea));
        Button removeButton = new Button(VaadinIcon.TRASH.create());
        removeButton.addClickShortcut(Key.DELETE);
        removeButton.addClickListener(e -> this.deleteTarea(tarea));
        layout.add(editButton,removeButton);
        return layout;
    }).setFlexGrow(0);
    public TareaView(@Autowired DataService dataService) {

        this.dataService = dataService;
        addClassNames("tarea-view", "flex", "flex-col", "h-full");
        setSizeFull();
        configureGrid();

        form = new TareaForm(dataService.findAllEstudiante());
        form.setWidth("25em");
        form.addListener(TareaForm.SaveEvent.class, this::saveTarea);
        form.addListener(TareaForm.CloseEvent.class, e -> closeEditor());

        FlexLayout content = new FlexLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setFlexShrink(0, form);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event
                -> editTarea(event.getValue()));
    }

    private void configureGrid() {

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(nombreColumn).setComponent(FiltrarNombre());
        headerRow.getCell(descripcionColumn).setComponent(FiltrarDescripcion());
        headerRow.getCell(fecha_inicioColumn).setComponent(FiltrarFechaInicio());
        headerRow.getCell(fecha_finColumn).setComponent(FiltrarFechaFin());
        headerRow.getCell(estudianteColumn).setComponent(FiltrarEstudiante());

        gridListDataView = grid.setItems(dataService.findAllTareas());
        grid.addClassNames("tarea-grid");
        grid.setSizeFull();
        grid.setHeightFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);

    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        Html total = new Html("<span>Total: <b>" + dataService.countEstudiante() + "</b> tareas</span>");

        Button addButton = new Button("Añadir Tarea", VaadinIcon.USER.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(click -> addTarea());

        HorizontalLayout toolbar = new HorizontalLayout(total, addButton);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.setWidth("100%");
        toolbar.expand(total);

        return toolbar;
    }

    private void saveTarea(TareaForm.SaveEvent event) {
        dataService.saveTarea(event.getTarea());
        updateList();
        closeEditor();
    }

    private void deleteTarea(Tarea tarea) {
        if (tarea == null)
            return;
        dataService.deleteTarea(tarea);
        this.refreshGrid();
    }
    private void refreshGrid() {
        if (dataService.findAllArea().size() > 0) {
            grid.setVisible(true);
            grid.setItems(dataService.findAllTareas());
        } else {
            grid.setVisible(false);
        }
    }

    public void editTarea(Tarea tarea) {
        if (tarea == null) {
            closeEditor();
        } else {
            form.setTarea(tarea);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    void addTarea() {
        grid.asSingleSelect().clear();
        editTarea(new Tarea());
    }

    private void closeEditor() {
        form.setTarea(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(dataService.findAllTareas());
    }

    // Filtros
    private TextField FiltrarNombre() {

        TextField filterNombre = new TextField();
        filterNombre.setPlaceholder("Filtrar");
        filterNombre.setClearButtonVisible(true);
        filterNombre.setWidth("100%");
        filterNombre.setValueChangeMode(ValueChangeMode.EAGER);
        filterNombre.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea -> StringUtils.containsIgnoreCase(tarea.getNombre(), filterNombre.getValue()))
        );

        return filterNombre;
    }

    private TextField FiltrarDescripcion() {
        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.EAGER);
        filterDescripcion.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea -> StringUtils.containsIgnoreCase(tarea.getDescripcion(), filterDescripcion.getValue()))
        );
        return filterDescripcion;
    }

    private ComboBox<Estudiante> FiltrarEstudiante() {
        ComboBox<Estudiante> estudiantefilter = new ComboBox<>();
        estudiantefilter.setItems(dataService.findAllEstudiante());
        estudiantefilter.setItemLabelGenerator(Estudiante::getStringNombreApellidos);
        estudiantefilter.setPlaceholder("Filtrar");
        estudiantefilter.setClearButtonVisible(true);
        estudiantefilter.setWidth("100%");
        estudiantefilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea ->  areEstudiantesEqual( tarea, estudiantefilter))
        );

        return estudiantefilter;
    }

    private boolean areEstudiantesEqual(Tarea tarea, ComboBox<Estudiante> filterEstudiante) {
        String estudianteFilterValue = filterEstudiante.getValue().getStringNombreApellidos();
        if (estudianteFilterValue != null) {
            return StringUtils.equals(tarea.getE().getStringNombreApellidos(), estudianteFilterValue);
        }
        return true;
    }

    private DatePicker FiltrarFechaInicio() {
        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(e->{
            grid.setItems(dataService.searchTareaByFecha( dateFilter.getValue() ) );
        });
        return dateFilter;
    }

    private DatePicker FiltrarFechaFin() {
        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchTareaByFecha( dateFilter.getValue() ) );
        });
        return dateFilter;
    }

}
