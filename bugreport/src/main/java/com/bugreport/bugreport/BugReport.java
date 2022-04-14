package com.bugreport.bugreport;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BugReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    public Integer pageId;

    public String title;

    public String description;

    public BugReport() {
    }
    
    public BugReport(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString(){
        return id + " | " + title + " | " + description;
    } 
}
