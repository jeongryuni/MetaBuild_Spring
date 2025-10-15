package com.example.Ex02.controller;

import com.example.Ex02.mapper.PersonMapper;
import com.example.Ex02.dto.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PersonController {

    // PersonMapper 인터페이스를 Spring 컨테이너에서 자동 주입받음
    @Autowired
    private PersonMapper personMapper;

    // 👉 [C] Create
    @GetMapping(value="/form")
    public String form(){
        return "form";
    }

    // 👉 [C] Create
    // form.html에서 입력한 값 → PersonDto에 자동 바인딩
    @PostMapping(value="/insertProc")
    public String insertProc(PersonDto pDto){
        personMapper.insertPerson(pDto);
        return "redirect:/list"; // list요청 get방식 ==>  목록 페이지로 이동 (재요청)
    }

    // 👉 [R] Read (전체 조회)
    // DB에서 전체 회원 조회 후 list.html로 전달
    @GetMapping(value="/list")
    public String list(Model model){
        List<PersonDto> lists = personMapper.selectAll();
        model.addAttribute("lists", lists); // 모델에 데이터 담기
        return "list";
    }

    // 👉 [R] Read (상세 조회)
    // num 값을 받아 DB에서 단일 회원 조회 후 content_view.html로 전달
    @GetMapping(value = "content_view")
    public String content_view(@RequestParam("num") int num, Model model){
        System.out.println("num :" + num);
        PersonDto pDto = personMapper.findByNum(num);
        model.addAttribute("pDto", pDto);
        return "content_view";
    }

    // 👉 [U] Update
    //  // content_view.html에서 수정한 데이터 전달받아 update 실행
    @PostMapping(value = "/modify")
    public String modify(PersonDto pDto){ // 3가지(num, name, age)
        int cnt = personMapper.updatePerson(pDto);
        System.out.println("cnt "+ cnt);
        return "redirect:/list";  // 수정 후 다시 목록으로 리다이렉트
    }

    // 👉 [D] Delete
    @GetMapping(value = "/delete")
    public String delete(@RequestParam("num")int num){
        int cnt = personMapper.deletePerson(num);
        return "redirect:/list"; // 삭제 후 다시 목록으로 리다이렉트
    }


}
