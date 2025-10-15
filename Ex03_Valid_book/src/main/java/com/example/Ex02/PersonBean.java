package com.example.Ex02;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonBean {

    @NotEmpty(message="이름 입력 누락")
    private String name;

    @Size(min=3,max=5,message = "3자리~5자리 입력")
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}