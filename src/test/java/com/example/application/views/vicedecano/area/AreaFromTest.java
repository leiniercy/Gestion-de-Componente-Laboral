/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.area;

import com.example.application.data.entity.Area;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Leinier
**/

public class AreaFromTest {

    private List<Area> listArea;
    private Area area1;
    private Area area2;

    /*
    @Before
    agrega datos ficticios que se utilizan para las pruebas 
    este metodo se ejecuta antes de cada metodo @Test
     */
    @Before
    public void setupData() {

        listArea = new ArrayList<>();

        area1 = new Area();
        area1.setId(1);
        area1.setNombre("Area 1");
        area1.setDescripcion("Mi Area1");

        area2 = new Area();
        area2.setId(1);
        area2.setNombre("Area 2");
        area2.setDescripcion("Mi Area2");

        listArea.add(area1);
        listArea.add(area2);

    }

    /*
     JUnit:
     -> Valida que los campos se completen correctamente, primero inicializando el formulario de area
     con algunas areas y luego configurando un bean de area para el formulario
     -> assertEquals() para comparar los valores de los campos disponibles
    */
    @Test
    public void formFieldsPopulated() {
        AreaForm form = new AreaForm();
        form.setArea(area1);
        Assert.assertEquals("Area 1", form.nombre.getValue());
        Assert.assertEquals("Mi Area1", form.descripcion.getValue());
    }

    /*
    * Funcionalidad guardar:
    * 1: Inicialice el formulario con un area vacío .
    * 2: Rellene los valores en el formulario.
    * 3: Capture el area guardada en una AtomicReference .
    * 4: Haga clic en el botón Guardar y lea el contacto guardado.
    * 5: Una vez que los datos del evento estén disponibles, verifique que el bean contenga los valores esperados.
    */

    @Test
    public void saveEventHasCorrectValues() {
        AreaForm form = new AreaForm();
        Area a = new Area();
        form.setArea(a);
        form.nombre.setValue("prueba");
        form.descripcion.setValue("Mi prueba unitaria");

        AtomicReference<Area> savAreaRef = new AtomicReference<>(null);
        form.addListener(AreaForm.SaveEvent.class, e -> {
            savAreaRef.set(e.getArea());
        });
        form.save.click();

        Area saveArea = savAreaRef.get();

        Assert.assertEquals("prueba", saveArea.getNombre());
        Assert.assertEquals("Mi prueba unitaria", saveArea.getDescripcion());
    }

}
