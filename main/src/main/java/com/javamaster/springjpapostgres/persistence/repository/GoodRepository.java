//package com.javamaster.springjpapostgres.persistence.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
//public interface GoodRepository extends JpaRepository<Good, Integer>, JpaSpecificationExecutor<Good> {
//    List<Good> findAllByName(String name);
//    Good findById(Long id);
//    @Query(value = "select name,price from good", nativeQuery = true)
//    List<Object> findNamePriceAll();
//    @Modifying
//    Good save(Good good);
//    @Modifying
//    @Query(value = "delete from good g where g.id = ?1", nativeQuery = true)
//    void deleteById(Long id);
//
//}
