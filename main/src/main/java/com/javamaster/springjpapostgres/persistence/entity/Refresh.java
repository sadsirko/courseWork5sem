package com.javamaster.springjpapostgres.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name  = "refresh")
public class Refresh {
    @Id
    String id;
    @Column
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Refresh{" +
                "id='" + id + '\'' +
                ", date=" + date +
                '}';
    }

}
