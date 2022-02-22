package com.javamaster.springjpapostgres.business.service;

import com.google.gson.Gson;
import com.javamaster.springjpapostgres.persistence.entity.Category;
import com.javamaster.springjpapostgres.persistence.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;
    private final RefreshService refreshService;

    public CategoryService(CategoryRepository categoryRepository, RefreshService refreshService) {
        this.categoryRepository = categoryRepository;
        this.refreshService = refreshService;
    }

    public Category findById(Integer id){return  categoryRepository.findById(id).orElse(null);}

    public Category findByName(String name){return  categoryRepository.findByName(name);}


    public List<Category> findAll(Specification<Category> criteria){return categoryRepository.findAll(criteria);};

    public void saveList( List<Category> data){
        for(int i = 0; i < data.size();i++){
            categoryRepository.save(data.get(i));
        }
    }

//    public List<Category> findAll(){
//        List<Category> res = new ArrayList<>();
//        if (refreshService.isCooldownGone()){
//            categoryRepository.deleteAll();
//            categoryRepository.saveAll(categories());
//            refreshService.refresh();
//        }
//        return categoryRepository.findAll();
//    };

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

    public void categories() {
        final String url = "http://localhost:3000/categories";
        RestTemplate restTemplate = new RestTemplate();
        String  category = restTemplate.getForObject(url, String.class);
        System.out.println(category);
        List<String> categoryList =  cutJSONArr(category);
        List<Category> sourceList = new ArrayList<>();
        String text;
        for (int i = 0; i < categoryList.size() ;i++){
//            sourceList.add(new Gson().fromJson(categoryList.get(i), Category.class));
            for(int j = 1; j < categoryList.get(i).length; j++){
                int hexVal = Integer.parseInt(arr[j], 16);
                text += (char)hexVal;
            }
            System.out.println(categoryList.get(i));
//            System.out.println(sourceList.get(i));
        }

        //        return sourceList;
    }




    @Transactional
    public void delete( Integer id){
        categoryRepository.deleteById(id);
    }



}
