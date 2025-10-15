package com.example.Ex02.controller;


import com.example.Ex02.dto.FootballDto;
import com.example.Ex02.mapper.FootballMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class FootballController {

    // FootballMapper라는 도구(인터페이스)를 스프링이 자동으로 가져와서 연결해 준다.
    // 내가 직접 new FootballMapper() 해서 만들 필요 없이,
    // 스프링이 알아서 footballMapper 변수에 준비된 객체를 넣어준다.

    /*인터페이스 풋볼 매퍼 참조변수 (첫글자 소문자 지정)*/
    @Autowired
    private FootballMapper footballMapper;

    @GetMapping(value = "/write")
    public String write(@ModelAttribute("fb") FootballDto FDto){

        return "write";
    }

    // write.html submit 클릭시 (writeProc요청)
    @PostMapping(value = "/writeProc")
    public String proc(@ModelAttribute("fb") @Valid FootballDto fb, BindingResult rs){

        String page="";
        if(rs.hasErrors()){
            return "write";
        }
        footballMapper.insertFootball(fb);
        return "redirect:/list";
        // 리스트[] DB삽입시 ==> 문자열 형태로 바꿔줘야함
        // 예) [미국, 한국]

    }

    // 리스트 조회
    @GetMapping(value = "/list")
    public String list(Model model,
                       @RequestParam(value ="whatColumn", required = false) String whatColumn,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page",defaultValue = "1") int page){


        // 한 페이지당 레코드3개
        int limit = 5;
        int offset = (page-1) * limit;

        Map<String, Object> params = new HashMap<String, Object>(); //key, value
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);

        List<FootballDto> lists = footballMapper.selectAll(params);
        int totalCount = footballMapper.getCount(params);
        int totalPage = (int) Math.ceil((double) totalCount / limit);

        model.addAttribute("lists",lists);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("whatColumn", whatColumn);
        model.addAttribute("page", page);

        return "list";
    }

    // 상세보기 이동
    @GetMapping(value = "content_view")
    public String content_view(@RequestParam("num") int num, Model model,
                               @RequestParam(value ="whatColumn", required = false) String whatColumn,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "page",required = false) int page
                               ){

        FootballDto fDto = footballMapper.findByNum(num);
        model.addAttribute("fDto", fDto);
        model.addAttribute("keyword", keyword);     // 검색어 유지
        model.addAttribute("whatColumn", whatColumn); // 검색 조건 유지
        model.addAttribute("page", page);          // 현재 페이지

        return "content_view";
    }

    // 수정폼 이동
    @GetMapping(value = "/update")
    public String modify(@RequestParam("num") int num, Model model,
                         @RequestParam(value ="whatColumn", required = false) String whatColumn,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "page",defaultValue = "1") int page
                         ){ // num을 reqeust받고 int num에 담음, model에 담기

        FootballDto fDto = footballMapper.findByNum(num);
        model.addAttribute("fDto", fDto);

        model.addAttribute("nationList", List.of("한국","미국","독일","스페인"));
        model.addAttribute("round16List", List.of("한국","멕시코","독일","브라질", "스위스", "잉글랜드"));

        model.addAttribute("keyword", keyword);     // 검색어 유지
        model.addAttribute("whatColumn", whatColumn); // 검색 조건 유지
        model.addAttribute("page", page);          // 현재 페이지

        return "update";
    }

    //수정 후 목록으로 이동
    @PostMapping(value = "/updateProc")
    public String update(@ModelAttribute("fDto") @Valid FootballDto fDto,BindingResult rs, Model model,
                         @RequestParam(value ="whatColumn", required = false) String whatColumn,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "page",defaultValue = "1") int page){
        model.addAttribute("fDto", fDto);

        model.addAttribute("nationList", List.of("한국","미국","독일","스페인"));
        model.addAttribute("round16List", List.of("한국","멕시코","독일","브라질", "스위스", "잉글랜드"));
        model.addAttribute("keyword", keyword);     // 검색어 유지
        model.addAttribute("whatColumn", whatColumn); // 검색 조건 유지
        model.addAttribute("page", page);          // 현재 페이지

        if(rs.hasErrors()){
            return "update";
        }
        footballMapper.updateFootball(fDto);

        String resultPage = "";
        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/list?page="+page+"&whatColumn="+whatColumn+"&keyword="+encodeKeyword;
    }
    @GetMapping(value = "/delete")
    public String delete(@RequestParam("num") int num,
                         @RequestParam(value ="whatColumn", required = false) String whatColumn,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "page",defaultValue = "1") int page){

        footballMapper.deleteFootball(num);

        String resultPage = "";
        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/list?page="+page+"&whatColumn="+whatColumn+"&keyword="+encodeKeyword;
    }


}
