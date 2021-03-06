package com.javamaster.springjpapostgres.persistence.repository;

import com.javamaster.springjpapostgres.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    Optional<Category> findById(Integer id);
    List<Category> findAll();
    Category findByName(String name);
    @Transactional
    @Modifying
    @Query(value = "insert into category(name) values(?1)",
            nativeQuery = true)
    void save(String name);
}
