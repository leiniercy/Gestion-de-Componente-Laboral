package com.example.application;


import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;

import com.example.application.data.repository.UserRepository;
import com.example.application.data.entity.User;
import com.example.application.data.entity.Rol;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "myapp")
@PWA(name = "My App", shortName = "My App", offlineResources = {"images/logo.png"})
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

   @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        Logger logger = LoggerFactory.getLogger(getClass());
         if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            logger.info("Generating demo data");
            logger.info("... generating 2 User entities...");
        createUser1("Leinier","admin", "admin");
        createUser2("Otro","user", "user");
    }

    private User createUser1(String name, String username, String password ) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Stream.of(Rol.USER, Rol.ADMIN).collect(Collectors.toSet()));
        userRepository.saveAndFlush(user);
        return user;
    }
    private User createUser2(String name, String username, String password ) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(Rol.USER));
        userRepository.saveAndFlush(user);
        return user;
    }

}
