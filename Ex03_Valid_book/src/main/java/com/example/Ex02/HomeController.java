package com.example.Ex02;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    @RequestMapping(value="/")
    public String home() {

        return "home";
    }

    @RequestMapping(value="/form")
    public String form(Model model) {
        PersonBean per = new PersonBean();
        model.addAttribute("per",per);
        return "form";
    }
    // form.html에서 submit요청
    @PostMapping(value="formProc")
    public  String formProc(@ModelAttribute("per") @Valid PersonBean pb, BindingResult br){
        String page="";
        System.out.println("br.hasErrors(): " + br.hasErrors());

        if(br.hasErrors()){ // 유효성 문제생기면 true 없으면 false
            page = "form"; //true
        }else{
            page="result"; //false
        }
        return page;
    }

}