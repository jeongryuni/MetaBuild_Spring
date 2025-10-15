package com.example.Ex02;

import com.example.Ex02.Dto.MusicBean;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    // home.html
    // http://localhost:9292/
    @RequestMapping(value= "/", method = RequestMethod.GET)
    public String home(){
        return "home";
    }

    // http://localhost:9292/form
    //@RequestMapping(value="/input", method = RequestMethod.GET)
    @GetMapping(value = "/form")
    public String form(){
        return "form";
    }

    // @RequestMapping(value="/input", method = RequestMethod.POST)
    // http://localhost:9292/music/result1
    @PostMapping(value="/input1")
    public String input(HttpServletRequest request, Model model){

        String title = request.getParameter("title");
        String singer = request.getParameter("singer");
        String price = request.getParameter("price");

        // 챙겨서 넘어갈때 Model속성
        request.setAttribute("rtitle",title);
        model.addAttribute("mtitle",title);

        request.setAttribute("rsinger",singer);
        model.addAttribute("msinger",singer);

        request.setAttribute("rprice",price);
        model.addAttribute("mprice",price);

        System.out.println(title+"/"+singer + "/"+price);
        return "music/result1";
    }

    @RequestMapping(value= "/input2", method = RequestMethod.POST)
    // @PostMapping(value="/input2")
    public String input2(@RequestParam("title")String title,
                         @RequestParam("singer")String singer,
                         @RequestParam("price")int price,
                         Model model) {

        System.out.println(title + ", " + singer + ", " + price);

        MusicBean mb = new MusicBean();
        mb.setTitle(title);
        mb.setSinger(singer);
        mb.setPrice(price);

        model.addAttribute("music", mb);

        return "music/result2";
    }

    @RequestMapping(value="input3", method = RequestMethod.POST)
    // @PostMapping(value="/input3")
    public ModelAndView input3(HttpServletRequest request){

        String title = request.getParameter("title");
        String singer = request.getParameter("singer");
        String price = request.getParameter("price");

        // model.addAttribute 와 같다
        // 하나씩 설정
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", title);
        mav.addObject("singer", singer);
        mav.addObject("price", price);
        mav.addObject("addr", "서울");

        // 묶음 설정
        MusicBean mb = new MusicBean();
        mb.setTitle(title);
        mb.setSinger(singer);
        mb.setPrice(Integer.parseInt(price));
        mav.addObject("mavMusic",mb);

        // setViewName : 넘어갈 곳 작성
        mav.setViewName("music/result3");

        return mav;
    }


    @PostMapping("/input4")
    public String input4(MusicBean mb) {
        // MusicBean mb: command 객체(아래 8줄의 의미를 갖고있음)
        /*String title = request.getParameter("title");
        String singer = request.getParameter("singer");
        String price = request.getParameter("price");
        MusicBean mb = new MusicBean();
        mb.setTitle(title);
        mb.setSinger(singer);
        mb.setPrice(Integer.parseInt(price));
        mav.addObject("musicBean",mb);*/  //소문자 시작: musicBean

        return "music/result4";
    }

    // @ModelAttribute 클래스명 별칭 지정
    // 별칭사용시 기존 클래스명 사용불가X 별칭만 사용가능
    @PostMapping("/input5")
    public String input5(@ModelAttribute("bean") MusicBean mb){


        return "music/result5";
    }

}