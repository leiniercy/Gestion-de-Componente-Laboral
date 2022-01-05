package com.example.application.data.repository;

import com.example.application.data.entity.SamplePerson;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SamplePersonRepository extends JpaRepository<SamplePerson, Integer> {
      @Query("select c from SamplePerson c " +
        "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
        "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<SamplePerson> search(@Param("searchTerm") String searchTerm);
}