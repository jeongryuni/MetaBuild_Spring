package com.example.Ex02.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class BreadDto {
    private int id;

    @NotBlank(message = "상품명을 입력하세요")
    private String name; // 빵 이름
    @NotBlank(message = "고유코드를 입력하세요")
    private String code;// 빵 고유코드 B001
    @NotBlank(message = "설명을 입력하세요")
    private String description; // 빵 설명
    @Size(min=1, message = "하나 이상 선택하세요")
    private List<String> ingredients; // checkbox (재료 체크 선택)
    @NotBlank(message = "보관방법을 선택하세요")
    private String storageType; // radio (상온/냉장/냉동 보관)

    @NotEmpty(message = "카테고리를 선택하세요")
    private String category; // select-option (식사빵/간식빵/디저트빵)

    @Min(value = 500, message = "500원 이상 입력하세요")
    private int price; // 빵 가격
    @Min(value = 0)
    private int stock; // 빵 재고
    @NotBlank(message = "출시일을 입력하세요")
    private String date; // 빵 출시일

    private String ingredientsAsString;

    public String getIngredientsAsString() {
        return (ingredients != null? String.join(",", ingredients): "");
    }

    public void setIngredientsAsString(String ingredientsAsString) {
        this.ingredients = Arrays.asList(ingredientsAsString.split(","));
    }
    // 이미지 문자열 저장
    private String image;

    // 이미지 파일저장
    private MultipartFile file;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
