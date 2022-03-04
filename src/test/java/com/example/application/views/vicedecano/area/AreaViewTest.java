package com.example.application.views.vicedecano.area;

import com.example.application.data.entity.Area;
import com.example.application.data.service.AreaService;
import com.vaadin.flow.component.grid.Grid;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Leinier
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaViewTest {

    private Grid<Area> grid;
    private AreaForm form;


    private List<Area> areas;
    private Area area1;
    private Area area2;

    /*
    @Before
    agrega datos ficticios que se utilizan para las pruebas
    este metodo se ejecuta antes de cada metodo @Test
     */
    @Before
    public void setupData() {

        areas = new ArrayList<>();

        area1 = new Area();
        area1.setId(1);
        area1.setNombre("Area 1");
        area1.setDescripcion("Mi Area1");

        area2 = new Area();
        area2.setId(1);
        area2.setNombre("Area 2");
        area2.setDescripcion("Mi Area2");

        areas.add(area1);
        areas.add(area2);

    }

    /*
     * afirmar que la formulario está inicialmente oculto
     * seleccionar el primer elemento de la tabla y verificando que:
     *   el formulario esta visible
     *   asegurarse que el carea seleccionada sea la misma  que el valor que se encuentra en el campo de formulario
     */


    //Hay que comentariar la  notificacion de area modificada xq da error
    //Area modificada
    @Test
    public void SetAreaSelected() {

        grid = new Grid<>(Area.class);
        grid.setItems(areas);
        form = new AreaForm();

        grid.asSingleSelect().setValue(area1);

        form.setArea(area1);
        form.nombre.setValue("prueba");

        AtomicReference<Area> savAreaRef = new AtomicReference<>(null);
        form.addListener(AreaForm.SaveEvent.class, e -> {
            savAreaRef.set(e.getArea());
        });
        form.save.click();

        Area saveArea = savAreaRef.get();

        Assert.assertEquals("prueba", saveArea.getNombre());

    }

    //Aun no termindada

    @Autowired
    AreaService areaService;
    @Test
    public void DeleteAreaSelected() {

        grid = new Grid<>(Area.class);
        grid.setItems(areas);
        form = new AreaForm();

        grid.asSingleSelect().setValue(area2);

        AtomicReference<Area> DeleteAreaRef = new AtomicReference<>(null);
        form.addListener(AreaForm.DeleteEvent.class, e -> {
            DeleteAreaRef.set(e.getArea());
        });
    }


}
