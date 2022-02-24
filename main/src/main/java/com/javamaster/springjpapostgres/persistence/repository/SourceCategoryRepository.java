package com.javamaster.springjpapostgres.persistence.repository;

import com.javamaster.springjpapostgres.persistence.entity.Category;
import com.javamaster.springjpapostgres.persistence.entity.SourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SourceCategoryRepository extends JpaRepository<SourceCategory, Integer>, JpaSpecificationExecutor<SourceCategory> {
    List<SourceCategory> findAll();
//    List<SourceCategory> findByCategory(Integer category);
    @Transactional
    @Modifying
    @Query(value = "insert into source_category (source_Id,category_Id) values(?1,?2)",
            nativeQuery = true)
    void save(String source_Id, Integer category_Id);
}
