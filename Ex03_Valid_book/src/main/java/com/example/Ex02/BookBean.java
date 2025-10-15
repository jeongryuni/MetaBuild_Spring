package com.example.Ex02;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;
@Setter
@Getter
public class BookBean {
    @NotBlank(message ="제목 입력 누락")
    private String title;

    //@Size(min=3, max=5, message = "3~5자리 이하로 입력")
    @NotEmpty(message ="저자 입력 누락")
    @Length(min=5, message = "5글자 이상 입력")
    private String author;
    
    @Pattern(regexp = "^[0-9]+$", message = "price는 숫자로 입력하세요")
    @NotEmpty(message = "price 누락")
    private String price;
    
    @NotEmpty(message = "출판사 입력 누락")
    private String publisher;

    @NotEmpty(message = "서점 입력 누락")
    private List<String> bookstore;

    // ---- getter & setter ----
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public List<String> getBookstore() { return bookstore; }
    public void setBookstore(List<String> bookstore) { this.bookstore = bookstore; }
}

