package com.example.Ex02.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 매개변수가 없는 생성자
@AllArgsConstructor // 매개변수가 모두 있는 생성자
@Table(name = "eTest") // 테이블 이름 새로 지정
@Entity // 클래스 이름을 보고 테이블 자동생성
public class EntityTest {
    @Id // PK(Primary Key) 지정
    private int num;

    @Column(name = "irum", nullable = false)
    private String name;

    private String addr;
    private int age;

    @Override
    public String toString() {
        return "EntityTest{" +
                "num=" + num +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", age=" + age +
                '}';
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
