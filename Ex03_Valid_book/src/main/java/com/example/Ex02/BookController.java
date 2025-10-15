package com.example.Ex02;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookController {

    @RequestMapping(value = "bookform")
    public String book(Model model){
        BookBean bb = new BookBean();
        model.addAttribute("book", bb);
        return "book/form";
    }
    @RequestMapping(value = "bookProc")
    // @ModelAttribute("book") 별칭과 form.html의 th:object="${book}"와 반드시 같아야함
    public String book2(@ModelAttribute("book") @Valid BookBean bb, BindingResult br){
        
        System.out.println("br.hasErrors(): " + br.hasErrors());
        String page="";
        if(br.hasErrors()){
            page="book/form";
        }else{
            page="book/result";
        }
        return page;
    }


}
