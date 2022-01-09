package com.example.application.views.area;

import com.example.application.data.DataService;
import com.example.application.data.entity.Area;
import com.example.application.data.service.AreaService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Area")
@Route(value = "area-form/:areaID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("admin")
public class AreaView extends Div implements BeforeEnterObserver {

    private final String AREA_ID = "areaID";
    private final String AREA_EDIT_ROUTE_TEMPLATE = "area-form/%d/edit";

    private Grid<Area> grid = new Grid<>(Area.class, false);
    Grid.Column<Area> nombreColumn = grid.addColumn(Area::getNombre);
    Grid.Column<Area> descricpcionColumn = grid.addColumn(Area::getDescripcion);

    private TextField nombre;
    private TextField descripcion;

    private Button save = new Button("Añadir");
    private Button cancel = new Button("Cancelar");
    private Button delete = new Button("Eliminar");

    private BeanValidationBinder<Area> binder;

    private Area area;

    private AreaService areaService;

    private DataService dataService;

    public AreaView(
            @Autowired AreaService areaService,
            @Autowired DataService dataService
    ) {
        this.areaService = areaService;
        this.dataService = dataService;
        addClassNames("area-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        nombreColumn.setHeader("Nombre").setAutoWidth(true);
        descricpcionColumn.setHeader("Descripción").setAutoWidth(true);

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(nombreColumn).setComponent(FiltrarNombre());
        headerRow.getCell(descricpcionColumn).setComponent(FiltrarDescripcion());

        grid.setItems(query -> areaService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(AREA_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(AreaView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Area.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        //Button
        cancel.addClickShortcut(Key.ESCAPE);
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickShortcut(Key.ENTER);
        save.addClickListener(e -> {
            try {
                if (this.area == null) {
                    this.area = new Area();
                }
                binder.writeBean(this.area);

                areaService.update(this.area);
                clearForm();
                refreshGrid();
                Notification.show("Area añadida.");
                UI.getCurrent().navigate(AreaView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the area details.");
            }
        });

        delete.addClickShortcut(Key.DELETE);
        delete.addClickListener(e -> {
            try {
                if (this.area == null) {
                    this.area = new Area();
                }
                binder.writeBean(this.area);

                areaService.delete(this.area);
                clearForm();
                refreshGrid();
                Notification.show("Area Eliminada.");
                UI.getCurrent().navigate(AreaView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the area details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> areaId = event.getRouteParameters().getInteger(AREA_ID);
        if (areaId.isPresent()) {
            Optional<Area> areaFromBackend = areaService.get(areaId.get());
            if (areaFromBackend.isPresent()) {
                populateForm(areaFromBackend.get());
            } else {
                Notification.show(String.format("The requested area was not found, ID = %d", areaId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(AreaView.class);
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
        descripcion = new TextField("Descripcion");

        Component[] fields = new Component[]{nombre, descripcion};

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

    private void populateForm(Area value) {
        this.area = value;
        binder.readBean(this.area);

    }

    //Filtros
    private TextField FiltrarNombre() {

        TextField filterNombre = new TextField();
        filterNombre.setPlaceholder("Nombre..");
        filterNombre.setClearButtonVisible(true);
        filterNombre.setValueChangeMode(ValueChangeMode.LAZY);
        filterNombre.addValueChangeListener(e -> {
            grid.setItems(dataService.searchAreaByName(filterNombre.getValue()));
        });

        return filterNombre;
    }

    private TextField FiltrarDescripcion() {
        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Descripción..");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDescripcion.addValueChangeListener(e -> {
            grid.setItems(dataService.searchAreaByDescripcion(filterDescripcion.getValue()));
        });
        return filterDescripcion;
    }

}
