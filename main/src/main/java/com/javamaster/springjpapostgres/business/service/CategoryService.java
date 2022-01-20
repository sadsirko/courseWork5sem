package com.javamaster.springjpapostgres.business.service;

import com.javamaster.springjpapostgres.persistence.entity.Category;
import com.javamaster.springjpapostgres.persistence.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Integer id){return  categoryRepository.findById(id).orElse(null);}

    public Category findByName(String name){return  categoryRepository.findByName(name);}

    public List<Category> findAll(){return categoryRepository.findAll();};

    public List<Category> findAll(Specification<Category> criteria){return categoryRepository.findAll(criteria);};

    public void saveList( List<Category> data){
        for(int i = 0; i < data.size();i++){
            categoryRepository.save(data.get(i));
        }
    }


    @Transactional
    public void delete( Integer id){
        categoryRepository.deleteById(id);
    }



}
