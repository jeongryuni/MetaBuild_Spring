package com.example.Ex02;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
public class ProductBean {

    @NotBlank(message = "아이디 입력 누락")
    private String id;

    @Size(min=3, max=5 , message = "3~5자리 이하로 입력하세요.")
    @NotBlank(message = "비밀번호 입력 누락")
    private String passwd;

    @Size(min=1, message = "하나 이상 선택하세요")
    private List<String> product;

    @NotBlank(message = "배송시간 누락")
    private String deliveryTime;

    @NotBlank(message = "결제방법 선택 누락")
    private String radio;

    @AssertTrue(message = "동의 누락")
    private boolean agree;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPasswd() { return passwd; }
    public void setPasswd(String passwd) { this.passwd = passwd; }

    public List<String> getProduct() { return product; }
    public void setProduct(List<String> product) { this.product = product; }

    public String getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(String deliveryTime) { this.deliveryTime = deliveryTime; }

    public String getRadio() { return radio; }
    public void setRadio(String radio) { this.radio = radio; }

    public boolean isAgree() { return agree; }   // ✅ boolean getter는 isAgree
    public void setAgree(boolean agree) { this.agree = agree; }
}