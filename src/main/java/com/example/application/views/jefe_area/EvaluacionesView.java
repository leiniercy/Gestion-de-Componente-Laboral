package com.example.application.views.jefe_area;

import com.example.application.data.DataService;
import com.example.application.data.entity.Grupo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Tarea;
import com.example.application.data.service.EvaluacionService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;

@PageTitle("Evaluaciones")
@Route(value = "evaluaciones-view/:evaluaionesID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("jefeArea")
public class EvaluacionesView extends Div implements BeforeEnterObserver {

    private final String EVALUACION_ID = "evaluaionesID";
    private final String EVALUACION_EDIT_ROUTE_TEMPLATE = "evaluaciones-view/%d/edit";

    private Grid<Evaluacion> grid = new Grid<>(Evaluacion.class, false);

    private TextField nota;
    private TextArea descripcion;
    private ComboBox<Estudiante> estudiante;
    private ComboBox<Tarea> tarea;
    private ComboBox<String> status;

    private Button save = new Button("Añadir");
    private Button cancel = new Button("Cancelar");
    private Button delete = new Button("Eliminar");

    private BeanValidationBinder<Evaluacion> binder;

    private Evaluacion evaluacion;

    private EvaluacionService evaluacionService;

    private DataService dataService;

    private Grid.Column<Evaluacion> notaColumn = grid.addColumn(Evaluacion::getNota).setHeader("Nota").setAutoWidth(true);
    private Grid.Column<Evaluacion> descripcionColumn = grid.addColumn(Evaluacion::getDescripcion).setHeader("Descripción").setAutoWidth(true);
    private Grid.Column<Evaluacion> estudianteColumn = grid.addColumn(evaluacion -> evaluacion.getEstudiante().getStringNombreApellidos()).setHeader("Estudiante").setAutoWidth(true);
    private Grid.Column<Evaluacion> tareaColumn = grid.addColumn(evaluacion -> evaluacion.getTarea().getNombre()).setHeader("Tarea").setAutoWidth(true);
    private Grid.Column<Evaluacion> statusColumn = grid.addColumn(evaluacion -> evaluacion.getStatus()).setHeader("Status").setAutoWidth(true);

    public EvaluacionesView(
            @Autowired EvaluacionService evaluacionService,
            @Autowired DataService dataService
    ) {
        this.dataService = dataService;
        this.evaluacionService = evaluacionService;
        addClassNames("evaluacion-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(notaColumn).setComponent(FiltrarNota());
        headerRow.getCell(descripcionColumn).setComponent(FiltrarDescripcion());
        headerRow.getCell(estudianteColumn).setComponent(FiltrarEstudinate());
        headerRow.getCell(tareaColumn).setComponent(FiltarTarea());
        headerRow.getCell(statusColumn).setComponent(FiltarStatus());

        grid.setItems(query -> evaluacionService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(EVALUACION_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(EvaluacionesView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Evaluacion.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        estudiante.setItems(dataService.findAllEstudiante());
        estudiante.setItemLabelGenerator(Estudiante::getStringNombreApellidos);

        tarea.setItems(dataService.findAllTareas());
        tarea.setItemLabelGenerator(Tarea::getNombre);

//        List<String> list_status = new ArrayList<>();
//        list_status.add("pendiente");
//        list_status.add("completada");
//        list_status.add("no completatda");

        status.setItems(Arrays.asList("Pendiente", "Completada", "No Completada"));

        //Button
        cancel.addClickShortcut(Key.ESCAPE);
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickShortcut(Key.ENTER);
        save.addClickListener(e -> {
            try {
                if (this.evaluacion == null) {
                    this.evaluacion = new Evaluacion();
                }
                binder.writeBean(this.evaluacion);

                evaluacionService.update(this.evaluacion);
                clearForm();
                refreshGrid();
                Notification.show("Evaluación añadida.");
                UI.getCurrent().navigate(EvaluacionesView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the tarea details.");
            }
        });

        delete.addClickShortcut(Key.DELETE);
        delete.addClickListener(e -> {
            try {
                if (this.evaluacion == null) {
                    this.evaluacion = new Evaluacion();
                }
                binder.writeBean(this.evaluacion);

                evaluacionService.delete(this.evaluacion);
                clearForm();
                refreshGrid();
                Notification.show("Evaluación eliminada.");
                UI.getCurrent().navigate(EvaluacionesView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the tarea details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> evaluacionId = event.getRouteParameters().getInteger(EVALUACION_ID);
        if (evaluacionId.isPresent()) {
            Optional<Evaluacion> evaluacionFromBackend = evaluacionService.get(evaluacionId.get());
            if (evaluacionFromBackend.isPresent()) {
                populateForm(evaluacionFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested evaluacion was not found, ID = %d", evaluacionId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(EvaluacionesView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        nota = new TextField("Nota");
        descripcion = new TextArea("Descripcion");
        estudiante = new ComboBox<>("Estudiante");
        tarea = new ComboBox<>("Tarea");
        status = new ComboBox<>("Status");
        Component[] fields = new Component[]{nota, descripcion, estudiante, tarea, status};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, delete, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Evaluacion value) {
        this.evaluacion = value;
        binder.readBean(this.evaluacion);

    }

    //Filtros
    private TextField FiltrarNota() {

        TextField filterNota = new TextField();
        filterNota.setPlaceholder("Filtrar");
        filterNota.setClearButtonVisible(true);
        filterNota.setWidth("100%");
        filterNota.setValueChangeMode(ValueChangeMode.LAZY);
        filterNota.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEvaluacionByNota(filterNota.getValue()));
        });

        return filterNota;
    }

    private TextField FiltrarDescripcion() {

        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDescripcion.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEvaluacionByDescripcion(filterDescripcion.getValue()));
        });
        return filterDescripcion;
    }

    private ComboBox<Estudiante> FiltrarEstudinate() {

        ComboBox<Estudiante> filterEstudiante = new ComboBox<>();
        filterEstudiante.setItems(dataService.findAllEstudiante());
        filterEstudiante.setItemLabelGenerator(Estudiante::getStringNombreApellidos);
        filterEstudiante.setPlaceholder("Filtrar");
        filterEstudiante.setClearButtonVisible(true);
        filterEstudiante.setWidth("100%");
        filterEstudiante.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEvaluacionByEstudiante(filterEstudiante.getValue().getStringNombreApellidos()));
        });
        return filterEstudiante;
    }

    private ComboBox<Tarea> FiltarTarea() {

        ComboBox<Tarea> filterTarea = new ComboBox<>();
        filterTarea.setItems(dataService.findAllTareas());
        filterTarea.setItemLabelGenerator(Tarea::getNombre);
        filterTarea.setPlaceholder("Filter");
        filterTarea.setClearButtonVisible(true);
        filterTarea.setWidth("100%");
        filterTarea.addValueChangeListener(e->{
            grid.setItems(dataService.searchEvaluacionByTarea(filterTarea.getValue().getNombre() ));
        });


        return filterTarea;
    }

     private ComboBox<String> FiltarStatus() {
        
        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.setItems(Arrays.asList("Pendiente", "Completada", "No Completada"));
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(e->{
             grid.setItems(dataService.searchEvaluacionByTarea(status.getValue()));
        });
      
        
        return statusFilter;
    }





}
