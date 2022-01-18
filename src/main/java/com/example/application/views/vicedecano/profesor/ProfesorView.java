package com.example.application.views.vicedecano.profesor;

import com.example.application.data.DataService;
import com.example.application.data.entity.Area;
import com.example.application.data.entity.Profesor;
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
import com.example.application.data.service.ProfesorService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.value.ValueChangeMode;
import javax.annotation.security.RolesAllowed;

@PageTitle("Profesor")
@Route(value = "profesor-view/:profesorID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("vicedecano")
public class ProfesorView extends Div implements BeforeEnterObserver {

    private final String PROFESOR_ID = "profesorID";
    private final String PROFESOR_EDIT_ROUTE_TEMPLATE = "profesor-view/%d/edit";

    private Grid<Profesor> grid = new Grid<>(Profesor.class, false);

    private TextField nombre;
    private TextField apellidos;
    private EmailField email;
    private TextField solapin;
    private ComboBox<Area> a;

    private Button save = new Button("Añadir");
    private Button cancel = new Button("Cancelar");
    private Button delete = new Button("Eliminar");

    private BeanValidationBinder<Profesor> binder;

    private Profesor profesor;

    private ProfesorService profesorService;

    private DataService dataService;

    private Grid.Column<Profesor> nombreColumn = grid.addColumn(Profesor::getNombre).setHeader("Nombre").setAutoWidth(true);
    private Grid.Column<Profesor> apellidosColumn = grid.addColumn(Profesor::getApellidos).setHeader("Apellidos").setAutoWidth(true);
    private Grid.Column<Profesor> emailColumn = grid.addColumn(Profesor::getEmail).setHeader("Correo").setAutoWidth(true);
    private Grid.Column<Profesor> solapinColumn = grid.addColumn(Profesor::getSolapin).setHeader("Solapín").setAutoWidth(true);
    private Grid.Column<Profesor> areaColumn = grid.addColumn(profesor -> profesor.getA().getNombre()).setHeader("Área").setAutoWidth(true);

    public ProfesorView(
            @Autowired ProfesorService profesorService,
            @Autowired DataService dataService) {

        this.profesorService = profesorService;
        this.dataService = dataService;
        addClassNames("profesor-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(nombreColumn).setComponent(FiltrarNombre());
        headerRow.getCell(apellidosColumn).setComponent(FiltrarApellidos());
        headerRow.getCell(emailColumn).setComponent(FiltrarEmail());
        headerRow.getCell(solapinColumn).setComponent(FiltrarSolapin());
        headerRow.getCell(areaColumn).setComponent(FiltrarArea());
        grid.setItems(query -> profesorService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROFESOR_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProfesorView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Profesor.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        a.setItems(dataService.findAllArea());
        a.setItemLabelGenerator(Area::getNombre);

        //Button
        cancel.addClickShortcut(Key.ESCAPE);
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickShortcut(Key.ENTER);
        save.addClickListener(e -> {
            try {
                if (this.profesor == null) {
                    this.profesor = new Profesor();
                }
                binder.writeBean(this.profesor);

                profesorService.update(this.profesor);
                clearForm();
                refreshGrid();
                Notification.show("Profesor añadido.");
                UI.getCurrent().navigate(ProfesorView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the profesor details.");
            }
        });

        delete.addClickShortcut(Key.DELETE);
        delete.addClickListener(e -> {
            try {
                if (this.profesor == null) {
                    this.profesor = new Profesor();
                }
                binder.writeBean(this.profesor);

                profesorService.delete(this.profesor);
                clearForm();
                refreshGrid();
                Notification.show("Profesor eliminado.");
                UI.getCurrent().navigate(ProfesorView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the profesor details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> profesorId = event.getRouteParameters().getInteger(PROFESOR_ID);
        if (profesorId.isPresent()) {
            Optional<Profesor> profesorFromBackend = profesorService.get(profesorId.get());
            if (profesorFromBackend.isPresent()) {
                populateForm(profesorFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %d", profesorId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProfesorView.class);
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
        apellidos = new TextField("Apellidos");
        solapin = new TextField("Solapin");
        email = new EmailField("Email");
        a = new ComboBox<>("Area");
        Component[] fields = new Component[]{nombre, apellidos, email, solapin, a};

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

    private void populateForm(Profesor value) {
        this.profesor = value;
        binder.readBean(this.profesor);

    }

    // Filtros
    private TextField FiltrarNombre() {

        TextField nombreFilter = new TextField();
        nombreFilter.setPlaceholder("Filtrar");
        nombreFilter.setClearButtonVisible(true);
        nombreFilter.setWidth("100%");
        nombreFilter.setValueChangeMode(ValueChangeMode.LAZY);
        nombreFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchProfesorByNombre(nombreFilter.getValue()));
        });

        return nombreFilter;
    }

    private TextField FiltrarApellidos() {
        TextField apellidosFilter = new TextField();
        apellidosFilter.setPlaceholder("Filtrar");
        apellidosFilter.setClearButtonVisible(true);
        apellidosFilter.setWidth("100%");
        apellidosFilter.setValueChangeMode(ValueChangeMode.LAZY);
        apellidosFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchProfesorByApellidos(apellidosFilter.getValue()));
        });
        return apellidosFilter;
    }

    private TextField FiltrarEmail() {
        TextField emailFilter = new TextField();
        emailFilter.setPlaceholder("Filtrar");
        emailFilter.setClearButtonVisible(true);
        emailFilter.setWidth("100%");
        emailFilter.setValueChangeMode(ValueChangeMode.LAZY);
        emailFilter.addValueChangeListener(e -> {
             grid.setItems(dataService.searchProfesorByEmail(emailFilter.getValue()));
        });
        return emailFilter;
    }

    private TextField FiltrarSolapin() {
        TextField solapinFilter = new TextField();
        solapinFilter.setPlaceholder("Filtrar");
        solapinFilter.setClearButtonVisible(true);
        solapinFilter.setWidth("100%");
        solapinFilter.setValueChangeMode(ValueChangeMode.LAZY);
        solapinFilter.addValueChangeListener(e -> {
             grid.setItems(dataService.searchProfesorBySolapin(solapinFilter.getValue()));
        });
        return solapinFilter;
    }

    private ComboBox<Area> FiltrarArea() {
        ComboBox<Area> areaFilter = new ComboBox<>();
        areaFilter.setItems(dataService.findAllArea());
        areaFilter.setItemLabelGenerator(Area::getNombre);
        areaFilter.setPlaceholder("Filtrar");
        areaFilter.setClearButtonVisible(true);
        areaFilter.setWidth("100%");
        areaFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchProfesorByArea(areaFilter.getValue().getNombre() ));
        });
        return areaFilter;
    }
}

