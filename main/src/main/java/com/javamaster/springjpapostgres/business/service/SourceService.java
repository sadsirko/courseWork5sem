package com.javamaster.springjpapostgres.business.service;

import com.javamaster.springjpapostgres.business.service.specification.SourceSpecificationsBuilder;
import com.javamaster.springjpapostgres.persistence.entity.Source;
import com.javamaster.springjpapostgres.persistence.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
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
    
    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public Source findById(String id){return  sourceRepository.findById(id).orElse(null);}
    public Source findByName(String name){return  sourceRepository.findByName(name);}
    public List<Source> findAll(){return sourceRepository.findAll();};

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
//            JSONObject obj = new JSONObject(jsonArr.substring(start,end));
//            String pageName = obj.getJSONObject("pageInfo").getString("pageName");
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


    public void addChannel(String link) {
        final String url = "http://localhost:3000/add?link=" + link;
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
