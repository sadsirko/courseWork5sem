package com.javamaster.springjpapostgres.persistence.repository;

import com.javamaster.springjpapostgres.persistence.entity.Category;
import com.javamaster.springjpapostgres.persistence.entity.Refresh;
import com.javamaster.springjpapostgres.persistence.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface  RefreshRepository extends JpaRepository<Refresh, String>, JpaSpecificationExecutor<Refresh> {
    Refresh findByDate(Date date);
    @Query(value = "select modtime from refresh order by id desc limit 1",
            nativeQuery = true)
    Date lastCashDate();
    @Transactional
    @Modifying
    @Query(value = "insert into refresh default values ",
            nativeQuery = true)
    void refresh();
}
