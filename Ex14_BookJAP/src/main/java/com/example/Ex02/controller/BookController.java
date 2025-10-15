package com.example.Ex02.controller;

import com.example.Ex02.bean.BookBean;
import com.example.Ex02.entity.BookEntity;
import com.example.Ex02.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class BookController {

    // (1) BookService를 주입받음 (비즈니스 로직 처리 담당)
    @Autowired
    BookService bookService;

    @ModelAttribute("kindArr")
    public List<String> kind(){
        List<String> kindList = new LinkedList<>();
        kindList.add("유료");
        kindList.add("무료");
        return kindList;
    }

    @ModelAttribute("bookstoreArr")
    public List<String> bookstore(){
        List<String> bookstoreList = new LinkedList<>();
        bookstoreList.add("교보문고");
        bookstoreList.add("알라딘");
        bookstoreList.add("yes24");
        bookstoreList.add("인터파크");
        return bookstoreList;
    }
    // 1. 도서 목록 조회 + 검색 + 페이징
    @GetMapping(value = {"/", "blist"}) // (2) URL의 쿼리 파라미터를 받음
    public String getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String keyword,
            Model model
    ){

        // (3) DB의 전체 도서 수 조회
        long totalCount = bookService.count();
        System.out.println("totalCount : " + totalCount);


        // (4) Service를 통해 현재 페이지에 표시할 도서 목록을 가져옴 :  Page Class
        Page<BookEntity> bookList = bookService.getBookEntity(page,size,keyword); // 현재 페이지에 출력할 3가지

        // (5) 디버깅용 로그 출력 (페이징 정보)
        System.out.println("bookList.getTotalElements() :" +bookList.getTotalElements()); //전체 데이터 개수
        System.out.println("bookList.getTotalPages() :" +bookList.getTotalPages()); // 전체 페이지 수
        System.out.println("bookList.getNumber() :" + bookList.getNumber()); // 현재 페이지 번호(0부터 시작)
        System.out.println("bookList.getNumberOfElements()" + bookList.getNumberOfElements()); // 현재 페이지 레코드 개수

        // (6) 화면(view)에 전달할 데이터 등록
        model.addAttribute("bookList", bookList);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalCount", totalCount);

        return "book/select";
    }

    // 2. 도서 등록 폼으로 이동
    @GetMapping(value = "book/insert")
    public String insertBook(Model model){
        model.addAttribute("bookBean", new BookBean());
        return "book/insert";
    }

    // 3. 도서 등록 처리
    @PostMapping(value = "/book/insert")
    public String insertBookProc(@Valid@ModelAttribute("bookBean") BookBean bookBean, BindingResult br, Model model){

        if(br.hasErrors()){
            return "book/insert";
        }
        //bookBean을 Entity로 바꾸는 작업
        BookEntity bookEntity = bookService.beanToEntity(bookBean);
        bookService.saveBook(bookEntity);
        return "redirect:/blist";
    }

    // 4. 목록 => 번호선택시 상세페이지 이동
    @GetMapping(value = "book/detail")
    public String detailBook(
            Model model,
            @RequestParam("no")int no,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String keyword
           ){


        BookEntity bookBean = bookService.getBookByNo(no);

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("book", bookBean);
        return "book/detail";
    }

    // 5. 목록 => 수정클릭시 수정페이지 이동
    @GetMapping(value = "book/update")
    public String updateBook(
            Model model,
            @RequestParam("no")int no,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String keyword
    ){
        BookEntity bookEntity = bookService.getBookByNo(no);

 /*       String kindArr[] = {"유료", "무료"};
        String bookstoreArr[] = {"교보문고","알라딘","yes24","인터파크"};*/


        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("bookBean", bookEntity);
/*        model.addAttribute("kindList", kindArr);
        model.addAttribute("bookstoreList", bookstoreArr);*/

        return "book/update";
    }


    // 수정처리
    @PostMapping(value = "/book/update")
    public String updateBookProc(Model model,
            @Valid@ModelAttribute("bookBean") BookBean bookBean, BindingResult br,
            @RequestParam("no")int no,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String keyword
    ){
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        if(br.hasErrors()){
            return "book/update";
        }
        BookEntity bookEntity = bookService.beanToEntity(bookBean);
        bookService.updateBook(bookEntity);
        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/blist?page="+page+"&size="+size+"&keyword="+encodeKeyword;
    }

    // 삭제 처리
    @GetMapping(value = "book/delete")
    public String deletelBook(
            Model model,
            @RequestParam("no")int no,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String keyword
    ){

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        bookService.deleteBook(no);

        Page<BookEntity> bookList = bookService.getBookEntity(page,size,keyword);
        if(bookList.getNumberOfElements()==0){
            page = page-1;
        }

        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/blist?page="+page+"&size="+size+"&keyword="+encodeKeyword;
    }

    // 다중 삭제
    @PostMapping(value = "book/checkDelete")
    public String deleteSelect(
            Model model,
            @RequestParam("row") List<Integer> nos,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String keyword
    ){
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        // 여러 개 삭제 반복 수행 단일삭제에서 for문만 추가
        for (int no : nos) {
            bookService.deleteBook(no);  // 단일 삭제와 같은 메서드
        }

        Page<BookEntity> bookList = bookService.getBookEntity(page,size,keyword);
        if (bookList.getNumberOfElements() == 0 && page > 0){
            page = page-1;
        }

        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/blist?page="+page+"&size="+size+"&keyword="+encodeKeyword;
    }
}
