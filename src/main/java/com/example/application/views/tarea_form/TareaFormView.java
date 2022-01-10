package com.example.application.views.tarea_form;

import com.example.application.data.entity.Estudiante;
import com.example.application.data.DataService;
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
import com.example.application.data.entity.Tarea;
import com.example.application.data.service.TareaService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import javax.annotation.security.RolesAllowed;

@PageTitle("Tarea")
@Route(value = "tarea-form/:tareaID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("admin")
public class TareaFormView extends Div implements BeforeEnterObserver {

    private final String TAREA_ID = "tareaID";
    private final String TAREA_EDIT_ROUTE_TEMPLATE = "tarea-form/%d/edit";

    private Grid<Tarea> grid = new Grid<>(Tarea.class, false);

    private TextField nombre;
    private TextArea descripcion;
    private TextField duracion;
    private ComboBox<Estudiante> e;

    private Button save = new Button("Añadir");
    private Button cancel = new Button("Cancelar");
    private Button delete = new Button("Eliminar");

    private BeanValidationBinder<Tarea> binder;

    private Tarea tarea;

    private TareaService tareaService;

    private DataService dataService;

    private Grid.Column<Tarea> nombreColumn = grid.addColumn(Tarea::getNombre).setHeader("Nombre").setAutoWidth(true);
    private Grid.Column<Tarea> descripcionColumn = grid.addColumn(Tarea::getDescripcion).setHeader("Descripción").setAutoWidth(true);
    private Grid.Column<Tarea> duracionColumn = grid.addColumn(Tarea::getDuracion).setHeader("Duración").setAutoWidth(true);
    private Grid.Column<Tarea> estudianteColumn = grid.addColumn(tarea -> tarea.getE().getStringNombreApellidos()).setHeader("Estudiante").setAutoWidth(true);

    public TareaFormView(
            @Autowired TareaService tareaService,
            @Autowired DataService dataService) {
        this.dataService = dataService;
        this.tareaService = tareaService;
        addClassNames("tarea-form", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(nombreColumn).setComponent(FiltrarNombre());
        headerRow.getCell(descripcionColumn).setComponent(FiltrarDescripcion());
        headerRow.getCell(duracionColumn).setComponent(FiltrarDuracion());
        headerRow.getCell(estudianteColumn).setComponent(FiltrarEstudiante());
        
        grid.setItems(query -> tareaService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(TAREA_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TareaFormView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Tarea.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);


        e.setItems(dataService.findAllEstudiante());
        e.setItemLabelGenerator(Estudiante::getStringNombreApellidos);

        //Button
        cancel.addClickShortcut(Key.ESCAPE);
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickShortcut(Key.ENTER);
        save.addClickListener(e -> {
            try {
                if (this.tarea == null) {
                    this.tarea = new Tarea();
                }
                binder.writeBean(this.tarea);

                tareaService.update(this.tarea);
                clearForm();
                refreshGrid();
                Notification.show("Tarea añadida.");
                UI.getCurrent().navigate(TareaFormView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the tarea details.");
            }
        });

        delete.addClickShortcut(Key.DELETE);
        delete.addClickListener(e -> {
            try {
                if (this.tarea == null) {
                    this.tarea = new Tarea();
                }
                binder.writeBean(this.tarea);

                tareaService.delete(this.tarea);
                clearForm();
                refreshGrid();
                Notification.show("Tarea Eliminada.");
                UI.getCurrent().navigate(TareaFormView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the tarea details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> tareaId = event.getRouteParameters().getInteger(TAREA_ID);
        if (tareaId.isPresent()) {
            Optional<Tarea> tareaFromBackend = tareaService.get(tareaId.get());
            if (tareaFromBackend.isPresent()) {
                populateForm(tareaFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested tarea was not found, ID = %d", tareaId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(TareaFormView.class);
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
        nombre = new TextField("Nombre");
        descripcion = new TextArea("Descripcion");
        duracion = new TextField("Duración");
        e = new ComboBox<>("Estudiante");
        Component[] fields = new Component[]{nombre, descripcion, duracion, e};

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

    private void populateForm(Tarea value) {
        this.tarea = value;
        binder.readBean(this.tarea);

    }
    //Filtros

    private TextField FiltrarNombre() {

        TextField filterNombre = new TextField();
        filterNombre.setPlaceholder("Filtrar");
        filterNombre.setClearButtonVisible(true);
        filterNombre.setWidth("100%");
        filterNombre.setValueChangeMode(ValueChangeMode.LAZY);
        filterNombre.addValueChangeListener(e -> {
            grid.setItems(dataService.searchTareaByNombre(filterNombre.getValue()));
        });

        return filterNombre;
    }

    private TextField FiltrarDescripcion() {
        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDescripcion.addValueChangeListener(e -> {
            grid.setItems(dataService.searchTareaByDescripcion(filterDescripcion.getValue()));
        });
        return filterDescripcion;
    }

    private TextField FiltrarDuracion() {
        TextField filterDuracion = new TextField();
        filterDuracion.setPlaceholder("Filtrar");
        filterDuracion.setClearButtonVisible(true);
        filterDuracion.setWidth("100%");
        filterDuracion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDuracion.addValueChangeListener(e -> {
            grid.setItems(dataService.searchTareaByDuracion(filterDuracion.getValue()));
        });
        return filterDuracion;
    }

    private TextField FiltrarEstudiante() {
        TextField EstudianteDuracion = new TextField();
        EstudianteDuracion.setPlaceholder("Filtrar");
        EstudianteDuracion.setClearButtonVisible(true);
        EstudianteDuracion.setWidth("100%");
        EstudianteDuracion.setValueChangeMode(ValueChangeMode.LAZY);
        EstudianteDuracion.addValueChangeListener(e -> {
            grid.setItems(dataService.searchTareaByEstudiante(EstudianteDuracion.getValue()));
        });
        return EstudianteDuracion;
    }
}
