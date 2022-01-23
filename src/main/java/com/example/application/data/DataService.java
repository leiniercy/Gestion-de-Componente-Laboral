/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.Formula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

/**
 *
 * @author Leinier
 */
@Service
public class DataService {
    
    private UserRepository userRepository;
    private AreaRepository areaRepository;
    private EstudianteRepository estudianteRepository;
    private ProfesorRepository profesorRepository;
    private EvaluacionRepository evaluacionRepository;
    private GrupoRepository grupoRepository;
    private TareaRepository tareaRepository;

    public DataService(
            @Autowired UserRepository userRepository,
            @Autowired AreaRepository areaRepository,
            @Autowired EstudianteRepository estudianteRepository,
            @Autowired ProfesorRepository profesorRepository,
            @Autowired EvaluacionRepository evaluacionRepository,
            @Autowired GrupoRepository grupoRepository,
            @Autowired TareaRepository tareaRepository) {

        this.userRepository = userRepository;
        this.areaRepository = areaRepository;
        this.estudianteRepository = estudianteRepository;
        this.profesorRepository = profesorRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.grupoRepository = grupoRepository;
        this.tareaRepository = tareaRepository;
    }

    //Area
    public List<Area> findAllArea() {
        return areaRepository.findAll();
    }

    public List<Area> searchArea(String stringFilter) {
        return areaRepository.search(stringFilter);
    }

    public long countArea() {
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
    public List<Estudiante> findAllEstudiante() {
        return estudianteRepository.findAll();
    }
       
    public List<Estudiante> searchEstudiante(String searchTerm) {
        return estudianteRepository.searchEstudiante(searchTerm);
    }

    public long countEstudiante() {
        return estudianteRepository.count();
    }

    public void deleteEstudiante(Estudiante estudiante) {
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
    public List<Evaluacion> findAllEvaluacion() {
        return evaluacionRepository.findAll();
    }

    public List<Evaluacion> searchEvaluacion(String searchTerm) {
        return evaluacionRepository.search(searchTerm);
    }

    public long countEvaluacion() {
        return evaluacionRepository.count();
    }

    public void deleteEvaluacion(Evaluacion evaluacion) {
        evaluacionRepository.delete(evaluacion);
    }

    public void saveEvaluacion(Evaluacion evaluacion) {
        if (evaluacion == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        evaluacionRepository.save(evaluacion);
    }

    //Grupo
    public List<Grupo> findAllGrupo() {
        return grupoRepository.findAll();
    }

    public List<Grupo> searchGrupo(Integer searchTerm) {
        return grupoRepository.search(searchTerm);
    }

    public long countGrupo() {
        return grupoRepository.count();
    }

    public void deleteGrupo(Grupo grupo) {
        grupoRepository.delete(grupo);
    }

    public void saveGrupo(Grupo grupo) {
        if (grupo == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        grupoRepository.save(grupo);
    }

    //Tarea
    public List<Tarea> findAllTareas() {
        return tareaRepository.findAll();
    }

    public List<Tarea> searchTarea(String searchTerm) {
        return tareaRepository.search(searchTerm);
    }
    public List<Tarea> searchTareaByFecha(LocalDate searchTerm) {
        return tareaRepository.searchFecha(searchTerm);
    }

    public long countTarea() {
        return tareaRepository.count();
    }

    public void deleteTarea(Tarea tarea) {
        tareaRepository.delete(tarea);
    }

    public void saveTarea(Tarea tarea) {
        if (tarea == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        tareaRepository.save(tarea);
    }
    
    
    
    //Profesor
    
    public List<Profesor> findAllProfesor() {
        return profesorRepository.findAll();
    }
    
    public List<Profesor> searchProfesor(String searchTerm) {
        return profesorRepository.searchProfesor(searchTerm);
    }

     public long countProfesor() {
        return profesorRepository.count();
    }

    public void deleteProfesor(Profesor profesor) {
        profesorRepository.delete(profesor);
    }

    public void saveProfesor(Profesor profesor) {
        if (profesor == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        profesorRepository.save(profesor);
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

}
