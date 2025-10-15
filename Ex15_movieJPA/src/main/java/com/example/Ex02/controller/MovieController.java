package com.example.Ex02.controller;

import com.example.Ex02.bean.MovieBean;
import com.example.Ex02.entity.MovieEntity;
import com.example.Ex02.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    MovieService movieService;


    // 1. 영화목록 전체조회로 이동
    @GetMapping(value = {"/", "mlist"})
    public String select(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String keyword,
            Model model
            ){

        // 전체페이지 수 조회
        long totalCount = movieService.count();

        Page<MovieEntity> movielist = movieService.getMovieEntity(page, size, keyword);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("movieList", movielist);

        return "movie/select";
    }

    // 체크박스 항목 - 장르 (LinkedList)
    @ModelAttribute(name = "genreArr")
    public List<String> genre(){
        List<String> genreList = new LinkedList<>();
        genreList.add("공포");
        genreList.add("코미디");
        genreList.add("액션");
        genreList.add("애니메이션");
        return genreList;
    }

    @ModelAttribute("genreArr")
    public List<String> genreArr() {
        return List.of("공포", "코미디", "액션", "애니메이션");
    }

    @ModelAttribute("timeArr")
    public List<String> timeArr() {
        return List.of(
                "09:00~12:00",
                "12:00~15:00",
                "15:00~18:00",
                "18:00~21:00",
                "21:00~00:00",
                "00:00~03:00"
        );
    }

    @ModelAttribute("partnerArr")
    public List<Integer> partnerArr() {
        return List.of(1, 2, 3, 4, 5);
    }

    // 2. 영화 등록 폼으로 insert로 이동
    @GetMapping(value = "movie/insert")
    public String insert(@ModelAttribute("movieBean") MovieBean movieBean,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "3") int size,
                         @RequestParam(required = false) String keyword){

        return "movie/insert";
    }

    // insert추가
    @PostMapping(value = "/movie/insert")
    public String insertProc(@Valid @ModelAttribute("movieBean")MovieBean movieBean,BindingResult br, Model model) {
        if(br.hasErrors()){
            System.out.println(111);
            return "movie/insert";
        }
        MovieEntity movieEntity = movieService.beanToEntity(movieBean);
        movieService.saveMovie(movieEntity);
        return "redirect:/mlist";
    }

    // 상세페이지로 이동
    @GetMapping(value = "movie/detail")
    public String detail(Model model,
                         @RequestParam("num")int num,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "3") int size,
                         @RequestParam(required = false) String keyword){

        MovieEntity movieBean = movieService.getMovieByNum(num);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("movie",movieBean);

        return "movie/detail";
    }

    // 수정폼으로 이동
    @GetMapping(value = "movie/update")
    public String update(@ModelAttribute("movieBean") MovieBean movieBean, Model model,
                         @RequestParam("num")int num,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "3") int size,
                         @RequestParam(required = false) String keyword){

        MovieEntity movieEntity = movieService.getMovieByNum(num);

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("movieBean", movieEntity);

        return "movie/update";
    }

    // 수정처리
    @PostMapping(value = "/movie/updateProc")
    public String updateProc(@Valid @ModelAttribute("movieBean")MovieBean movieBean,BindingResult br, Model model,
                             @RequestParam("num")int num,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "3") int size,
                             @RequestParam(required = false) String keyword) {

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);


        if(br.hasErrors()){
            return "movie/update";
        }

        MovieEntity movieEntity = movieService.beanToEntity(movieBean);
        movieService.updateMovie(movieEntity);
        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/mlist?page="+page+"&size="+size+"&keyword="+encodeKeyword;
    }

    // 삭제처리
    @GetMapping(value = "movie/delete")
    public String deleteMovie(
        Model model,
        @RequestParam("num")int num,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size,
        @RequestParam(required = false) String keyword){

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        movieService.deleteMovie(num);

        Page<MovieEntity> movielist = movieService.getMovieEntity(page, size, keyword);
        if (movielist.getNumberOfElements() == 0 && page > 0){
            page = page-1;
        }
        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/mlist?page="+page+"&size="+size+"&keyword="+encodeKeyword;
    }



    // 다중 삭제
    @PostMapping(value = "/deleteSelect")
    public String deleteSelect(
            Model model,
            @RequestParam("row") List<Integer> nums,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String keyword
    ){
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        // 여러 개 삭제 반복 수행 단일삭제에서 for문만 추가
        for (int num : nums) {
            movieService.deleteMovie(num);  // 단일 삭제와 같은 메서드
        }

        Page<MovieEntity> movielist = movieService.getMovieEntity(page, size, keyword);
        if (movielist.getNumberOfElements() == 0 && page > 0){
            page = page-1;
        }
        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/mlist?page="+page+"&size="+size+"&keyword="+encodeKeyword;
    }
}
