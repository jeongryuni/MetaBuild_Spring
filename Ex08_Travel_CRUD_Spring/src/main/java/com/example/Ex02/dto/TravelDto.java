package com.example.Ex02.dto;

import jakarta.validation.constraints.*;

import java.util.Arrays;
import java.util.List;

public class TravelDto {

    private int num;

    @NotBlank(message = "이름 누락")
    private String name;

    @Min(value = 1, message = "나이는 최소 1살 이상이어야 합니다")
    @Max(value = 120, message = "나이는 최대 120살 이하여야 합니다")
    private int age;

    @NotEmpty(message = "관심지역은 하나이상 선택해야 합니다.")
    private List<String> area;

    @NotEmpty(message = "원하는 여행 타입을 선택해주세요")
    private String style;

    @NotEmpty(message = "원하는 가격을 선택하세요")
    private String price;

    private String areaAsString;

    public String getAreaAsString() {

        return (area !=null)? String.join(",", area) : null;
    }

    public void setAreaAsString(String areaAsString) {

        this.area = Arrays.asList(areaAsString.split(","));
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
