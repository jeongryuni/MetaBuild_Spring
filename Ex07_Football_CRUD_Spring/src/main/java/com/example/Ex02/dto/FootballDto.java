package com.example.Ex02.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Arrays;
import java.util.List;

public class FootballDto {

    private int num;
    @NotBlank(message="id 입력 누락")
    private String id;
    @NotBlank(message="pw 입력 누락")
    private String pw;
    @NotEmpty(message="우승예상국가 입력 누락")
    private String win;
/*    @NotEmpty(message="16강예상국가 입력 누락")*/
    @NotEmpty(message = "1개이상 선택")
    private List<String> round16; // [한국,미국,멕시코]
    /*
    * List<String> [한국,미국,멕시코] 형태론 DB에 삽입이 되지않아 하나씩 뽑아서 삽입
    *
    * xml파일에서 ResultMap으로 작업
    * */


    private String round16AsString; // 한국,미국,멕시코

    public String getRound16AsString() {
        // return round16AsString;
        return (round16 != null) ? String.join(",",round16):null;
    }

    public void setRound16AsString(String round16AsString) {
        // this.round16AsString = round16AsString;
        this.round16 = Arrays.asList(round16AsString.split(","));
        //List<String> round16[브라질, 스위스]
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public List<String> getRound16() {
        return round16;
    }

    public void setRound16(List<String> round16) {
        this.round16 = round16;
    }
}