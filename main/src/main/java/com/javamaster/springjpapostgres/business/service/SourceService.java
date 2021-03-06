package com.javamaster.springjpapostgres.business.service;

import com.javamaster.springjpapostgres.business.service.specification.SourceSpecificationsBuilder;
import com.javamaster.springjpapostgres.persistence.entity.Source;
import com.javamaster.springjpapostgres.persistence.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;

@Service
public class SourceService {
    @Autowired
    private final SourceRepository sourceRepository;
    private final RefreshService refreshService;
    private final CategoryService categoryService;

    private final SourceCategoryService sourceCategoryService;
    public SourceService(SourceRepository sourceRepository, RefreshService refreshService, CategoryService categoryService, SourceCategoryService sourceCategoryService) {
        this.sourceRepository = sourceRepository;
        this.refreshService = refreshService;
        this.categoryService = categoryService;
        this.sourceCategoryService = sourceCategoryService;
    }

    public Source findById(String id){return  sourceRepository.findById(id).orElse(null);}
    public List<Source> findByName(String name){return  sourceRepository.findByName(name);}

    public List<Source> findAll(){
        List<Source> res = new ArrayList<>();
        if (refreshService.isCooldownGone()){
            System.out.println("refreshing");
            sourceCategoryService.deleteAll();
            sourceCategoryService.sourceCategories();
            sourceRepository.deleteAll();
            sourceRepository.saveAll(getDialogs());
            categoryService.categories();
            refreshService.refresh();
        }
        return sourceRepository.findAll();
    };

    public void deleteAll(){sourceRepository.deleteAll();}
    public List<Source> findAll(Specification<Source> criteria){return sourceRepository.findAll(criteria);};

    public void saveList( List<Source> data){
        for(int i = 0; i < data.size();i++){
            sourceRepository.save(data.get(i));
        }
    }

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

    public List<Source> getDialogs() {
        final String url = "http://localhost:3000/dialogs";
        RestTemplate restTemplate = new RestTemplate();
        String  source = restTemplate.getForObject(url, String.class);
        System.out.println(source);

        List<String> sourceStrList =  cutJSONArr(source);
        List<Source> sourceList = new ArrayList<>();
        for (int i = 0; i < sourceStrList.size() ;i++){
            sourceList.add(new Gson().fromJson(sourceStrList.get(i), Source.class));
            System.out.println(sourceStrList.get(i));
        }
        return sourceList;
    }

    public List<Source> findByCategory(String category){
        return sourceRepository.findByCategory(category);
    }

    public void process(String startDate, String endDate, String stop,
                        Integer symb_num, Integer range, List<Source> sourceList ){
        List<String> arr = new ArrayList<String>();
        for (int i = 0; i < sourceList.size(); i++){
            arr.add(sourceList.get(i).getId());
        }
        String url = """
                                http://localhost:3000/process?startDate="""+startDate+"""
                                &endDate=""" + endDate + """
                                &stop=""" + stop + """ 
                                &symbolNum="""+ symb_num +"""
                                &range="""+ range + """
                                &sourceList=""" + arr;

        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(url, String.class);

    }

    public void addChannel(String link) {
        final String url = "http://localhost:3000/add?link=" + link;
        System.out.println(link);
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        String  source = restTemplate.getForObject(url, String.class);
        System.out.println(source);
    }
    @Transactional
    public void delete( String id){
        sourceRepository.deleteById(id);
    }

    public List<Source> getSearch(String parameters){
        SourceSpecificationsBuilder builder = new SourceSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(parameters + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        Specification<Source> spec = builder.build();
        return findAll(spec);
    }
}
