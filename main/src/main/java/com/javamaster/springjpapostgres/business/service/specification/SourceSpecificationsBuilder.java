package com.javamaster.springjpapostgres.business.service.specification;

import com.javamaster.springjpapostgres.persistence.entity.Source;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SourceSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public SourceSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public SourceSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Source> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(SourceSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);
        for (int i = 1; i < params.size(); i++) {
            System.out.println(params.get(i).getOperation());
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
