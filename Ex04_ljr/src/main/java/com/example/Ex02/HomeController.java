package com.example.Ex02;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String home(){
        return "home";
    }

    // http://localhost:9292/form
    @GetMapping(value = "/form")
    public String form(){

        return"member/memberForm";
    }

    //  1. HttpServletRequest & Model 사용
    //  model속성, 각각 request속성 설정
    @PostMapping(value="/input1")
    public String input1(HttpServletRequest request, Model model){

        String name = request.getParameter("name");
        String age = request.getParameter("age");
        String [] hobby = request.getParameterValues("hobby");

/*        String[] hobby = request.getParameterValues("hobby"); // 요리, 게임
        String temp="";
        for(String hb : hobby){
            temp += hb+" ";

        request.setAttribute("rhobby",temp);
        model.addAttribute("mhobby",temp);

        }*/

        request.setAttribute("rname", name);
        model.addAttribute("mname", name);

        request.setAttribute("rage",age);
        model.addAttribute("mage", age);

        request.setAttribute("rhobby", hobby);
        model.addAttribute("mhobby", hobby);

        return "member/result1";
    }

    // 2.@RequestParam + VO/DTO 사용
    //  @RequestParam bean으로 묶고 bean을 model 속성 설정
    @PostMapping("/input2")
    public String input2(@RequestParam("name") String name,
                        @RequestParam("age") String age,
                        @RequestParam("hobby") List<String> hobby,
                        Model model){

        MemberBean mb = new MemberBean();
        mb.setAge(name);
        mb.setAge(age);
        mb.setHobby(hobby);

        model.addAttribute("member",mb);

        return "member/result2";
    }

    // 3. ModelAndView
    @PostMapping("/input3")
    public ModelAndView input3(HttpServletRequest request){
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        String[] hobby = request.getParameterValues("hobby");

        //  1. 객체 생성 후 값 설정
        MemberBean mb = new MemberBean();
        mb.setName(name);
        mb.setAge(age);
        mb.setHobby(List.of(hobby));

        ModelAndView mav = new ModelAndView();
        mav.addObject("mavMember", mb);

        // 2. 객체로 보내기
/*
        ModelAndView mav = new ModelAndView();

        mav.addObject("name", name);
        mav.addObject("age", age);
        mav.addObject("hobbyarr", hobbyarr);
*/

        mav.setViewName("member/result3");
        return mav;
    }

    // 4. command 객체로 만들어서 보내기
    @PostMapping("/input4")
    public String input4(MemberBean mb){
        return "member/result4";
    }

    // 5.  command 객체로 만들어서 별칭(@ModelAttribute)
    @PostMapping("/input5")
    public String input5(@ModelAttribute("mem") MemberBean mb){
        return "member/result5";
    }
}

