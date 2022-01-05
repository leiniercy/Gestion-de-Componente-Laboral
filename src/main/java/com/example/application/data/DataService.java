/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Leinier
 */

@Service
public class DataService {

    private AreaRepository areaRepository;
    private EstudianteRepository estudianteRepository;
    private EvaluacionRepository evaluacionRepository;
    private PersonRepository personRepository;
    private ProfesorRepository profesorRepository;
    private SamplePersonRepository samplePersonRepository;
    private TareaRepository tareaRepository;

    public DataService(
            @Autowired AreaRepository areaRepository,
            @Autowired EstudianteRepository estudianteRepository,
            @Autowired EvaluacionRepository evaluacionRepository,
            @Autowired PersonRepository personRepository,
            @Autowired ProfesorRepository profesorRepository,
            @Autowired SamplePersonRepository samplePersonRepository,
            @Autowired TareaRepository tareaRepository) {

        this.areaRepository = areaRepository;
        this.estudianteRepository = estudianteRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.personRepository = personRepository;
        this.profesorRepository = profesorRepository;
        this.samplePersonRepository = samplePersonRepository;
        this.tareaRepository = tareaRepository;
    }
    
    //Area
    public List<Area> findAllAreas() {
        return areaRepository.findAll();
    }

    public long countAreas() {
        return areaRepository.count();
    }

    public void deleteArea(Area area) {
        areaRepository.delete(area);
    }

    public void saveArea(Area area) {
        if (area == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        areaRepository.save(area);
    }

    //Estudiante
    public List<Estudiante> findAllEstudiantes() {
        return estudianteRepository.findAll();
    }
    
    public long countEstudiantes() {
        return estudianteRepository.count();
    }

    public void deleteArea(Estudiante estudiante) {
        estudianteRepository.delete(estudiante);
    }

    public void saveEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        estudianteRepository.save(estudiante);
    }
    
    //Evaluacion
    public List<Evaluacion> findAllEvaluaciones() {
        return evaluacionRepository.findAll();
    }
    
    public long countEvaluacion() {
        return evaluacionRepository.count();
    }

    public void deleteEvaluacion (Evaluacion evaluacion) {
        evaluacionRepository.delete(evaluacion);
    }

    public void saveEvaluacion(Evaluacion evaluacion) {
        if (evaluacion == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        evaluacionRepository.save(evaluacion);
    }
    
    //Person
    public List<Person> findAllPersonas() {
        return personRepository.findAll();
    }
    
    public long countPersonas() {
        return personRepository.count();
    }

    public void deletePerson(Person person) {
        personRepository.delete(person);
    }

    public void savePerson(Person person) {
        if (person == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        personRepository.save(person);
    }
    
    //Profesor
    public List<Profesor> findAllProfesores() {
        return profesorRepository.findAll();
    }
    
    public long countProfesores() {
        return profesorRepository.count();
    }

    public void deleteProfesores(Profesor profesor) {
        profesorRepository.delete(profesor);
    }

    public void saveProfesor(Profesor profesor) {
        if (profesor == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        profesorRepository.save(profesor);
    }

    //SamplePerson
    public List<SamplePerson> findAllSamplePerson() {
        return samplePersonRepository.findAll();
    }
    
    public long countSamplePersons() {
        return samplePersonRepository.count();
    }

    public void deleteSamplePerson(SamplePerson samplePerson) {
        samplePersonRepository.delete(samplePerson);
    }

    public void saveSamplePerson(SamplePerson samplePerson) {
        if (samplePerson == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        samplePersonRepository.save(samplePerson);
    }

    //Tarea
    public List<Tarea> findAllTareas() {
        return tareaRepository.findAll();
    }
    
    public long countTarea() {
        return tareaRepository.count();
    }

    public void deleteArea(Tarea tarea) {
        tareaRepository.delete(tarea);
    }

    public void saveArea(Tarea tarea) {
        if (tarea == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        tareaRepository.save(tarea);
    }

}
