package com.buggerpage.buggerpage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BuggerPage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    @Column(length = 50)
    public String name;

    @Column(length = 5000)
    public String description;

    public BuggerPage() {
    }
    
    public BuggerPage(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString(){
        return id + " | " + name + " | " + description;
    } 
}
