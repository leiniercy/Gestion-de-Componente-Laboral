package com.example.application.views.vicedecano.estudiante;

import com.example.application.data.entity.Area;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Grupo;
import com.example.application.data.entity.User;
import com.example.application.data.service.EstudianteService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
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
public class EstudianteViewTest {

    @Autowired
    private EstudiantesView estudiantesView;

    private Grid<Estudiante> grid;
    private EstudianteForm form;

    private List<Estudiante> estudiantes;
    private List<Area> areas;
    private List<User> users;
    private List<Grupo> grupos;
    private Estudiante estudiante1;
    private Estudiante estudiante2;
    private User u1;
    private User u2;
    private Grupo g1;
    private Grupo g2;
    private Area a1;
    private Area a2;


    @Before
    public void setupData() {


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
        estudiante2.setApellidos("Perez Perez");

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

    }


    //Modificar estudiante
    @Test
    public void SetEstudianteSelected() {

        grid = new Grid<>(Estudiante.class);
        grid.setItems(estudiantes);
        form = new EstudianteForm(users,areas,grupos);

        //selecciona el estudiante
        grid.asSingleSelect().setValue(estudiante2);

        form.setEstudiante(estudiante2);
        form.nombre.setValue("Joker");

        AtomicReference<Estudiante> saveEstudianteRef = new AtomicReference<>(null);
        form.addListener(EstudianteForm.SaveEvent.class, e -> {
            saveEstudianteRef.set(e.getEstudiante());
        });
        form.save.click();

        Estudiante saveEstudiante = saveEstudianteRef.get();

        Assert.assertEquals("Joker", saveEstudiante.getNombre());
    }

    //Delete Estudiante
    @Autowired
    EstudianteService estudianteService;
    @Test
    public void DeleteEstudianteSelected() {

        grid = new Grid<>(Estudiante.class);
        grid.setItems(estudiantes);
        form = new EstudianteForm(users,areas,grupos);

        //si el primer estudiante fue seleccionada elimninala
        grid.asSingleSelect().setValue(estudiante1);

        AtomicReference<Estudiante> deleteEstudianteRef = new AtomicReference<>(null);
        form.addListener(EstudianteForm.DeleteEvent.class, e -> {
            deleteEstudianteRef.set(e.getEstudiante());
        });

    }

}
