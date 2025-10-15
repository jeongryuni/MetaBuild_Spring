package com.example.Ex02.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ItemEntity { //item_entity 테이블이 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "itemName", nullable = false, length = 10)
    private String itemNm;

    private int price;
    private String itemDetail;
    private LocalDateTime regTime;

    @Override
    public String toString() {
        return "ItemEntity{" +
                "id=" + id +
                ", itemNm='" + itemNm + '\'' +
                ", price=" + price +
                ", itemDetail='" + itemDetail + '\'' +
                ", regTime=" + regTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemNm() {
        return itemNm;
    }

    public void setItemNm(String itemNm) {
        this.itemNm = itemNm;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(String itemDetail) {
        this.itemDetail = itemDetail;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }
}
