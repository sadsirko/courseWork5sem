package com.javamaster.springjpapostgres.persistence.repository;

import com.javamaster.springjpapostgres.persistence.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SourceRepository extends JpaRepository<Source, String>, JpaSpecificationExecutor<Source> {
    List<Source> findAll();
    Optional<Source> findById(String id);
    @Query(value = "select S from Source S where S.name like %?1%")
    List<Source> findByName(String name);

}
