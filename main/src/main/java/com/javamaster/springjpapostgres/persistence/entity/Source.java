package com.javamaster.springjpapostgres.persistence.entity;

import javax.persistence.*;
@Entity
@Table(name  = "source")
public class Source {
    @Id
    String id;
    @Column
    String name;
    @Column
    String link;
    @Column
    String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Source{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
