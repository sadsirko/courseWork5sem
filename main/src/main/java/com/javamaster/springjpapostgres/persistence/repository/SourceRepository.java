package com.javamaster.springjpapostgres.persistence.repository;

import com.javamaster.springjpapostgres.persistence.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SourceRepository extends JpaRepository<Source, String>, JpaSpecificationExecutor<Source> {
    Source findByName(String name);
    List<Source> findAll();
    Optional<Source> findById(String id);

}
