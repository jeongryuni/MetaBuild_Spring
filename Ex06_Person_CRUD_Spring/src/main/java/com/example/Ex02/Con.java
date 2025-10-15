package com.example.Ex02;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Con {
    @GetMapping(value="/")
    public String home() {

        return "home";
    }
}
