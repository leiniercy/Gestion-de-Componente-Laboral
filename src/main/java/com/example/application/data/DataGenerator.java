package com.example.application.data;

import com.example.application.data.entity.Area;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Grupo;
import com.example.application.data.entity.Tarea;
import com.example.application.data.repository.*;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(AreaRepository areaRepository, EvaluacionRepository evaluacionRepository,
            EstudianteRepository estudianteRepository, GrupoRepository grupoRepository, TareaRepository tareaRepository
            ) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (areaRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Area entities...");
            ExampleDataGenerator<Area> areaRepositoryGenerator = new ExampleDataGenerator<>(Area.class,
                    LocalDateTime.of(2022, 1, 10, 0, 0, 0));
            areaRepositoryGenerator.setData(Area::setId, DataType.ID);
            areaRepositoryGenerator.setData(Area::setNombre, DataType.WORD);
            areaRepositoryGenerator.setData(Area::setDescripcion, DataType.WORD);
            areaRepository.saveAll(areaRepositoryGenerator.create(50, seed));

            logger.info("... generating 100 Grupo entities...");
            ExampleDataGenerator<Grupo> grupoRepositoryGenerator = new ExampleDataGenerator<>(Grupo.class,
                    LocalDateTime.of(2022, 1, 10, 0, 0, 0));
            grupoRepositoryGenerator.setData(Grupo::setId, DataType.ID);
            grupoRepositoryGenerator.setData(Grupo::setNumero, DataType.WORD);
            grupoRepository.saveAll(grupoRepositoryGenerator.create(100, seed));

            

            logger.info("Generated demo data");
        };
    }

}