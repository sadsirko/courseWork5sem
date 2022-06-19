package com.javamaster.springjpapostgres.business.service;

import com.google.gson.Gson;
import com.javamaster.springjpapostgres.persistence.entity.Category;
import com.javamaster.springjpapostgres.persistence.entity.Source;
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
            System.out.println(data.get(i).getSource() + data.get(i).getCategory());
            sourceCategoryRepository.save(data.get(i).getSource(),data.get(i).getCategory());
        }
    }

    public void deleteAll(){sourceCategoryRepository.deleteAll();}

    public List<SourceCategory> findAll(){
        return sourceCategoryRepository.findAll();
    };
//
public List<String> cutJSONArr(String jsonArr) {
    List<String> resultArr = new ArrayList<>();
    int start = 0;
    int end = 0;
    while (end < jsonArr.length()-5)
    {
        start = jsonArr.indexOf("{",end);
        end = jsonArr.indexOf("}",end) + 1;
        resultArr.add(jsonArr.substring(start,end));
    }
    return resultArr;
}

    public List<SourceCategory> sourceCategories() {
        final String url = "http://localhost:3000/folders";
        RestTemplate restTemplate = new RestTemplate();
        String  category = restTemplate.getForObject(url, String.class);
        System.out.println("cat" + category);
        List<String> categoryList =  cutJSONArr(category);
        List<SourceCategory> realCategoryList = new ArrayList<>();
        for (int i = 0; i < categoryList.size() ;i++){
            System.out.println(categoryList.get(i));
            realCategoryList.add(new Gson().fromJson(categoryList.get(i), SourceCategory.class));
        }
        saveList(realCategoryList);
        return realCategoryList;
    }

    @Transactional
    public void delete( Integer id){
        sourceCategoryRepository.deleteById(id);
    }

}
