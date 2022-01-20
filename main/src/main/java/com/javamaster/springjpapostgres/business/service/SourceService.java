package com.javamaster.springjpapostgres.business.service;

import com.javamaster.springjpapostgres.business.service.specification.SourceSpecificationsBuilder;
import com.javamaster.springjpapostgres.persistence.entity.Source;
import com.javamaster.springjpapostgres.persistence.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
