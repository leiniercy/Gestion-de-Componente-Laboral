package com.example.application.views.jefe_area.tarea;


import com.example.application.data.entity.*;
import com.example.application.data.service.TareaService;
import com.example.application.views.jefe_area.evaluacion.EvaluacionForm;
import com.vaadin.flow.component.grid.Grid;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Leinier
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class TareaViewTest {


    private List<Tarea> tareas;
    private List<Estudiante> estudiantes;
    private List<Area> areas;
    private List<User> users;
    private List<Grupo> grupos;
    private Tarea tarea1;
    private Tarea tarea2;
    private Estudiante estudiante1;
    private Estudiante estudiante2;
    private User u1;
    private User u2;
    private Grupo g1;
    private Grupo g2;
    private Area a1;
    private Area a2;

    private Grid<Tarea> grid;
    private TareaForm form;


    @Before
    public void setupData() {

        estudiantes = new ArrayList<>();
        tareas = new ArrayList<>();

        estudiantes = new ArrayList<>();
        areas = new ArrayList<>();
        users = new ArrayList<>();
        grupos = new ArrayList<>();

        //Estudiante 1
        estudiante1 = new Estudiante();
        estudiante1.setNombre("Leinier");
        estudiante1.setApellidos("Caraballo Yanes");

        u1 = new User();
        u1.setName("Leinier");
        u1.setUsername("leiniercy");
        u1.setPassword("1234");
        users.add(u1);

        estudiante1.setUser(u1);
        estudiante1.setEmail("leiniercy@estudiantes.uci.cu");
        estudiante1.setSolapin("E170587");
        estudiante1.setAnno_repitencia(4);
        estudiante1.setCantidad_asignaturas(2);

        g1 = new Grupo();
        g1.setNumero("4401");
        grupos.add(g1);
        estudiante1.setGrupo(g1);

        a1 = new Area();
        a1.setNombre("Area 1");
        a1.setDescripcion("Mi Area1");
        areas.add(a1);

        estudiante1.setArea(a1);
        estudiantes.add(estudiante1);


        //Estudiante 2
        estudiante2 = new Estudiante();
        estudiante2.setNombre("Elegualdy");
        estudiante2.setApellidos("El Animal");

        u2 = new User();
        u2.setName("Elegualdy");
        u2.setUsername("elegualdy");
        u2.setPassword("1234");
        users.add(u2);

        estudiante2.setUser(u2);
        estudiante2.setEmail("elegualdy@estudiantes.uci.cu");
        estudiante2.setSolapin("E170588");
        estudiante2.setAnno_repitencia(1);
        estudiante2.setCantidad_asignaturas(3);

        g2 = new Grupo();
        g2.setNumero("4101");
        grupos.add(g2);
        estudiante2.setGrupo(g2);

        a2 = new Area();
        a2.setNombre("Area 2");
        a2.setDescripcion("Mi Area2");
        areas.add(a2);

        estudiante2.setArea(a2);

        //Tarea 1
        tarea1 = new Tarea();
        tarea1.setNombre("Tarea 1");
        tarea1.setDescripcion("Mi Tarea1");
        tarea1.setFecha_inicio(LocalDate.ofEpochDay(2022 - 03 - 1));
        tarea1.setFecha_fin(LocalDate.ofEpochDay(2022 - 03 - 11));
        tarea1.setE(estudiante1);
        tareas.add(tarea1);

        //Tarea 2
        tarea2 = new Tarea();
        tarea2.setNombre("Tarea 2");
        tarea2.setDescripcion("Mi Tarea2");
        tarea2.setE(estudiante2);
        tareas.add(tarea2);
    }

    @Test
    public void setTareaSelected() {

        Grid<Tarea> grid = new Grid<>(Tarea.class);
        grid.setItems(tareas);

        grid.asSingleSelect().setValue(tarea1);

        form = new TareaForm(estudiantes);
        form.setTarea(tarea1);
        form.nombre.setValue("Nueva Tarea");
        form.descripcion.setValue("Mi nueva tarea");
        form.e.setValue(estudiante2);

        AtomicReference<Tarea> saveTareaRef = new AtomicReference<>(null);
        form.addListener(TareaForm.SaveEvent.class, e -> {
            saveTareaRef.set(e.getTarea());
        });
        form.save.click();

        Tarea saveTarea = saveTareaRef.get();
        Assert.assertEquals("Nueva Tarea", saveTarea.getNombre());
        Assert.assertEquals("Mi nueva tarea", saveTarea.getDescripcion());
        Assert.assertEquals("Elegualdy", saveTarea.getE().getNombre());
    }

    @Test
    public void DeleteTareaSelected() {
        grid = new Grid<>(Tarea.class);
        form = new TareaForm(estudiantes);
        grid.setItems(tareas);
        grid.asSingleSelect().setValue(tarea1);

        AtomicReference<Tarea> deleteTareaRef = new AtomicReference<>(null);
        form.addListener(TareaForm.DeleteEvent.class, e -> {
            deleteTareaRef.set(e.getTarea());
        });
    }


}
