package com.example.application.views.vicedecano.estudiante;

import com.example.application.data.entity.Area;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Grupo;
import com.example.application.data.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Leinier
 **/

public class EstudianteFormTest {

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


    @Test
    public void formFieldsPopulated() {
        EstudianteForm form = new EstudianteForm(users, areas, grupos);
        form.setEstudiante(estudiante1);
        Assert.assertEquals("Leinier", form.nombre.getValue());
        Assert.assertEquals("Caraballo Yanes", form.apellidos.getValue());
        Assert.assertEquals("leiniercy", form.user.getValue().getUsername());
        Assert.assertEquals("leiniercy@estudiantes.uci.cu", form.email.getValue());
        Assert.assertEquals("E170587", form.solapin.getValue());
        Assert.assertEquals("4", form.anno_repitencia.getValue().toString());
        Assert.assertEquals("2", form.cantidad_asignaturas.getValue().toString());
        Assert.assertEquals("4401", form.grupo.getValue().getNumero());
        Assert.assertEquals("Area 1", form.area.getValue().getNombre());
    }

    //Añadir un estudiante
    @Test
    public void saveEventHasCorrectValues() {
        EstudianteForm form = new EstudianteForm(users, areas, grupos);
        Estudiante a = new Estudiante();
        form.setEstudiante(a);
        form.nombre.setValue("Elegualdy");
        form.apellidos.setValue("Perez Perez");
        form.user.setValue(u2);
        form.email.setValue("elegualdy@estudiantes.uci.cu");
        form.solapin.setValue("E170588");
        form.anno_repitencia.setValue(1);
        form.cantidad_asignaturas.setValue(3);
        form.grupo.setValue(g2);
        form.area.setValue(a2);

        AtomicReference<Estudiante> saveEstudianteRef = new AtomicReference<>(null);
        form.addListener(EstudianteForm.SaveEvent.class, e -> {
            saveEstudianteRef.set(e.getEstudiante());
        });
        form.save.click();

        Estudiante saveEstudiante = saveEstudianteRef.get();
        Assert.assertEquals("Elegualdy", saveEstudiante.getNombre());
        Assert.assertEquals("Perez Perez", saveEstudiante.getApellidos());
        Assert.assertEquals("elegualdy", saveEstudiante.getUser().getUsername());
        Assert.assertEquals("elegualdy@estudiantes.uci.cu", saveEstudiante.getEmail());
        Assert.assertEquals("E170588", saveEstudiante.getSolapin());
        Assert.assertEquals("1", saveEstudiante.getAnno_repitencia().toString());
        Assert.assertEquals("3", saveEstudiante.getCantidad_asignaturas().toString());
        Assert.assertEquals("4101", saveEstudiante.getGrupo().getNumero());
        Assert.assertEquals("Area 2", saveEstudiante.getArea().getNombre());
    }

}
