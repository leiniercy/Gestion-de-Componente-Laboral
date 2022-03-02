package com.example.application.views.vicedecano.area;

import com.example.application.data.entity.Area;
import com.example.application.data.service.AreaService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Leinier
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaViewTest {

    @Autowired
    private AreaView areaView;
    private Grid<Area> grid;


    /*
     * afirmar que la formulario está inicialmente oculto
     * seleccionar el primer elemento de la tabla y verificando que:
     *   el formulario esta visible
     *   asegurarse que el carea seleccionada sea la misma  que el valor que se encuentra en el campo de formulario
     */


    @Test
    public void formShownWhenAreaSelected() {
        grid = areaView.grid;
        Area firstArea = getFirtsItemToGrid(grid);
        AreaForm form = areaView.form;
        Assert.assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstArea);
        Assert.assertTrue(form.isVisible());
        Assert.assertEquals(firstArea.getNombre(), form.nombre.getValue());

    }

    //Hay que comentariar la  notificacion de area modificada xq da error
    //Area modificada
    @Test
    public void SetAreaSelected() {

        grid = areaView.grid;
        Area firstArea = getFirtsItemToGrid(grid);
        AreaForm form = areaView.form;
        Assert.assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstArea);
        Assert.assertTrue(form.isVisible());
        Assert.assertEquals(firstArea.getNombre(), form.nombre.getValue());
        Assert.assertEquals(firstArea.getDescripcion(), form.descripcion.getValue());

        form.setArea(firstArea);
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
        grid = areaView.grid;
        Area firstArea = getFirtsItemToGrid(grid);
        //si el primer area fue seleccionada elimninala
        grid.asSingleSelect().setValue(firstArea);
        areaService.deleteArea(firstArea);
    }

    private Area getFirtsItemToGrid(Grid<Area> grid) {
        return ((ListDataProvider<Area>) grid.getDataProvider()).getItems().iterator().next();
    }


}
