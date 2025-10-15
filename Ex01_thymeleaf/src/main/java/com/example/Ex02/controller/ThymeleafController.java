package com.example.Ex02.controller;

import com.example.Ex02.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/thymeleaf") // 요청명 앞에 항상 /thymeleaf가 붙게 만듬
public class ThymeleafController {

    @RequestMapping(value = "/ex01", method = RequestMethod.GET)
    public String Exam01(Model model){
        model.addAttribute("name","아이유");
        model.addAttribute("age",30);
        model.addAttribute("addr", "서울");
        return "view01"; // resources/templates/~~/html
    }
    // @RequestMapping(value= "/ex02", method = RequestMethod.GET)
    @GetMapping(value = "/ex02")
    public String Exam02(Model model){
        ItemDto itemDto = new ItemDto();
        itemDto.setNo(1);
        itemDto.setItemNm("상품");
        itemDto.setItemDetail("상품 설명");
        itemDto.setPrice(3000);
        model.addAttribute("itemDto",itemDto);
        return "view02";
    }

    @GetMapping("/ex03")
    public String Ex03(Model model){
        List<ItemDto> lists = new ArrayList<ItemDto>();
        for(int i=1; i<10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setNo(1);
            itemDto.setItemNm("상품");
            itemDto.setItemDetail("상품 설명");
            itemDto.setPrice(3000);
            lists.add(itemDto);

        }
        model.addAttribute("lists",lists);

        return "view03";
    }

    // http://localhost:9292/thymeleaf/ex04
    @GetMapping("/ex04")
    public String Ex04(Model model){
        return "thymeleaf/view04";
    }


    // http://localhost:9292/thymeleaf/ex05
    // http://localhost:9292/thymeleaf/ex05?param1=가나&param2=다라
    // 주소지에 매개변수 정확히 일치하기
    @GetMapping("/ex05")
    public String Ex05(Model model, String param1, String param2){
        if(param1 == null){
            param1 ="하하하";
        }
        if(param2 == null){
            param2 = "호호호";
        }

        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleaf/view05";
    }


}

