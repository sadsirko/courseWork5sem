package com.javamaster.springjpapostgres.persistence.entity;

import javax.persistence.*;
@Entity
@Table(name  = "source")
public class Source {
    @Id
    String id;
    @Column
    String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
