package com.javamaster.springjpapostgres.business.service;

import com.javamaster.springjpapostgres.persistence.entity.Category;
import com.javamaster.springjpapostgres.persistence.entity.SourceCategory;
import com.javamaster.springjpapostgres.persistence.repository.CategoryRepository;
import com.javamaster.springjpapostgres.persistence.repository.SourceCategoryRepository;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SourceCategoryService {
    @Autowired
    private final SourceCategoryRepository sourceCategoryRepository;

    public SourceCategoryService(SourceCategoryRepository sourceCategoryRepository) {
        this.sourceCategoryRepository = sourceCategoryRepository;
    }


//    public List<SourceCategory> findByCategory(Integer category){return  sourceCategoryRepository.findByCategory(category);}



    public void saveList( List<SourceCategory> data){
        for(int i = 0; i < data.size();i++){
            sourceCategoryRepository.save(data.get(i));
        }
    }
    public void deleteAll(){sourceCategoryRepository.deleteAll();}

    public List<SourceCategory> findAll(){
        return sourceCategoryRepository.findAll();
    };

    public List<String> cutJSONArr(String jsonArr) {
        List<String> resultArr = new ArrayList<>();
        int start = 0;
        int end = 0;
        while (end < jsonArr.length()-5)
        {
            start = jsonArr.indexOf("'",end);
            end = jsonArr.indexOf(",",end) + 1;
            resultArr.add(jsonArr.substring(start + 1,end - 2));
        }
        return resultArr;
    }
//
    public void sourceCategories() {
        final String url = "http://localhost:3000/folders";
        RestTemplate restTemplate = new RestTemplate();
        String  category = restTemplate.getForObject(url, String.class);
        System.out.println("cat" + category);
//        List<String> categoryList =  cutJSONArr(category);
//        List<Category> realCategoryList = new ArrayList<>();
//        deleteAll();
//        for (int i = 0; i < categoryList.size() ;i++){
//            System.out.println(StringEscapeUtils.unescapeJava(categoryList.get(i)));
//            SourceCategorycategoryRepository.save(StringEscapeUtils.unescapeJava(categoryList.get(i)));
//        }
    }




    @Transactional
    public void delete( Integer id){
        sourceCategoryRepository.deleteById(id);
    }



}
