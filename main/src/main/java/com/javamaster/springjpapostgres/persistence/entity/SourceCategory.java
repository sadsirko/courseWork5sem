package com.javamaster.springjpapostgres.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name  = "source_category")
public class SourceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "source_Id")
    private String source;

    @Column(name = "category_Id")
    private Integer category;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "SourceCategory{" +
                "source='" + source + '\'' +
                ", category=" + category +
                '}';
    }
}
