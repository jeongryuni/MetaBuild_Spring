package com.example.Ex02;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;

import java.util.List;

@Controller
public class Con_temp {
    @GetMapping(value="/")
    public String home() {

        return "home";
    }
    @GetMapping(value = "/form")
    public String form(@ModelAttribute("prod") ProductBean pb) {
        return "form";
    }
    @PostMapping(value = "submit")
    public String formProc(@ModelAttribute("prod") @Valid ProductBean pb, BindingResult br) {
        String page;
        List<ObjectError> lists = br.getAllErrors();
        for(ObjectError obj:lists){
            System.out.println("objectName: " + obj.getObjectName());
            System.out.println("code: " + obj.getCode());
            System.out.println("defaultMessage: " + obj.getDefaultMessage());
        }

        if (br.hasErrors()) {
            page = "form";
        } else {
            page = "result";
        }
        return page;
    }

}