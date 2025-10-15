package com.example.Ex02.controller;

import com.example.Ex02.dto.BreadDto;
import com.example.Ex02.mapper.BreadMapper;
import jakarta.annotation.PostConstruct;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BreadController {

    @Autowired
    BreadMapper breadMapper;

    // 이미지 저장 경로
    @Value("${upload.dir:${user.dir}/uploads}")
    private String uploadDir;

    // 업로드 폴더가 없으면 자동생성 (한번실행)
    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Paths.get(uploadDir));
    }

    // 홈 이동
    @GetMapping(value = "/breadList")
    public String home(
            @RequestParam(value = "whatColumn", required = false) String whatColumn,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model){

        int limit = 10;
        int offset = (page-1) * limit;

        Map<String, Object> params = new HashMap<>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);
        params.put("limit", limit);
        params.put("offset", offset);


        List<BreadDto> lists = breadMapper.breadSelectAll(params);
        int totalCount = breadMapper.getCount(params);
        int totalPage = (int)Math.ceil((double) totalCount/limit);

        model.addAttribute("lists",lists);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);

        return "bread/breadList";
    }

    // 홈에서 추가클릭 후 (insertForm)이동
    @GetMapping(value = "/breadInsert")
    public String breadinsert(@ModelAttribute("bDto")BreadDto bDto, Model model){

        String[] ingredientsArr = {
                "밀가루", "버터", "계란", "소금", "우유",
                "이스트", "초콜릿", "마가린", "연유", "햄", "단팥"};//재료
        String[] storageTypesArr = {"상온", "냉장", "냉동"}; //보관방식
        String[] categoriesArr = {"식사빵", "간식빵", "디저트빵"}; //카테고리

        model.addAttribute("ingredientsArr", ingredientsArr);
        model.addAttribute("storageTypesArr", storageTypesArr);
        model.addAttribute("categoriesArr", categoriesArr);
        model.addAttribute("bDto", bDto);

        return "bread/breadInsertForm";
    }

    // 입력처리
    @PostMapping(value = "/breadInsertProc")
    public String breadinsertproc(@ModelAttribute("bDto") @Valid BreadDto bDto, BindingResult br, Model model
     ){

        String page="";
        if (br.hasErrors()) {
            String[] ingredientsArr = {
                    "밀가루", "버터", "계란", "소금", "우유",
                    "이스트", "초콜릿", "마가린", "연유", "햄", "단팥"};//재료
            String[] storageTypesArr = {"상온", "냉장", "냉동"}; //보관방식
            String[] categoriesArr = {"식사빵", "간식빵", "디저트빵"}; //카테고리
            model.addAttribute("ingredientsArr", ingredientsArr);
            model.addAttribute("storageTypesArr", storageTypesArr);
            model.addAttribute("categoriesArr", categoriesArr);

            page = "bread/breadInsertForm";
        }else{
            breadMapper.insertBread(bDto);
            page = "redirect:/breadList";
        }
        return page;
    }

    // 상세보기 이동
    @GetMapping(value = "/breadContentView")
    public String breadContentView(@RequestParam("id") int id, Model model,
                                   @RequestParam(value = "whatColumn", required = false) String whatColumn,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "page", defaultValue = "1") int page
                                   ){


        String[] ingredientsArr = {
                "밀가루", "버터", "계란", "소금", "우유",
                "이스트", "초콜릿", "마가린", "연유", "햄", "단팥"};//재료
        String[] storageTypesArr = {"상온", "냉장", "냉동"}; //보관방식
        String[] categoriesArr = {"식사빵", "간식빵", "디저트빵"}; //카테고리

        BreadDto bDto = breadMapper.selectById(id);

        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("ingredientsArr", ingredientsArr);
        model.addAttribute("storageTypesArr", storageTypesArr);
        model.addAttribute("categoriesArr", categoriesArr);
        model.addAttribute("bDto", bDto);

        return "bread/breadContentView";
    }

    // 수정폼
    @GetMapping(value = "/breadUpdateForm")
    public String breadUpdateForm(@RequestParam("id") int id, Model model,
                                  @RequestParam(value = "whatColumn", required = false) String whatColumn,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "page", defaultValue = "1") int page){

        String[] ingredientsArr = {
                "밀가루", "버터", "계란", "소금", "우유",
                "이스트", "초콜릿", "마가린", "연유", "햄", "단팥"};//재료
        String[] storageTypesArr = {"상온", "냉장", "냉동"}; //보관방식
        String[] categoriesArr = {"식사빵", "간식빵", "디저트빵"}; //카테고리

        BreadDto bDto = breadMapper.selectById(id);
        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("ingredientsArr", ingredientsArr);
        model.addAttribute("storageTypesArr", storageTypesArr);
        model.addAttribute("categoriesArr", categoriesArr);
        model.addAttribute("bDto", bDto);

        return "bread/breadUpdateForm";
    }

    // 수정폼에서 수정완료누르기
    @PostMapping(value = "breadUpdateProc")
    public String breadUpdateProc(@ModelAttribute("bDto") @Valid BreadDto bDto, BindingResult br, Model model,
                                  @RequestParam(value = "whatColumn", required = false) String whatColumn,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "page", defaultValue = "1") int page)
                                  {

        if (br.hasErrors()){
            String[] ingredientsArr = {
                    "밀가루", "버터", "계란", "소금", "우유",
                    "이스트", "초콜릿", "마가린", "연유", "햄", "단팥"};//재료
            String[] storageTypesArr = {"상온", "냉장", "냉동"}; //보관방식
            String[] categoriesArr = {"식사빵", "간식빵", "디저트빵"}; //카테고리
            model.addAttribute("ingredientsArr", ingredientsArr);
            model.addAttribute("storageTypesArr", storageTypesArr);
            model.addAttribute("categoriesArr", categoriesArr);
            model.addAttribute("whatColumn",whatColumn);
            model.addAttribute("keyword",keyword);
            model.addAttribute("page",page);
            model.addAttribute("bDto", bDto);

            return "breadUpdateForm";
        }else {
             breadMapper.updateBread(bDto);

            String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
            return "redirect:/breadList?page="+page+"&whatColumn="+whatColumn+"&keyword="+encodeKeyword;
        }
    }

    @GetMapping(value = "breadDeleteProc")
    public String breadDeleteProc(
            @RequestParam("id") int id, Model model,
            @RequestParam(value = "whatColumn", required = false) String whatColumn,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page
    ){

        int limit = 10;
        int offset = (page-1) * limit;

        Map<String, Object> params = new HashMap<>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);
        params.put("limit", limit);
        params.put("offset", offset);

        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);

        breadMapper.deleteBread(id);
        int totalCount = breadMapper.getCount(params);
        if(totalCount % limit == 0){
            page = page - 1;
        }


        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/breadList?page="+page+"&whatColumn="+whatColumn+"&keyword="+encodeKeyword;
    }

    // 코드 중복체크
    @RequestMapping(value = "/checkCode")
    @ResponseBody
    public String CheckDuplicate(@RequestParam("code")String code){
        System.out.println("checkCode");

        int count = breadMapper.selectCountCode(code);
        System.out.println(count);

        String result = "";
        if(count > 0){
            result = "duplicate";
        }else{
            result = "available";
        }

        return result;
    }
}
