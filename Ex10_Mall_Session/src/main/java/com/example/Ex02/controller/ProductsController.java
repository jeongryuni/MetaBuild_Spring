package com.example.Ex02.controller;


import com.example.Ex02.dto.MemberDto;
import com.example.Ex02.dto.ProductsDto;
import com.example.Ex02.mapper.ProductsMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
public class ProductsController {

    // application.properties에서 업로드 경로 가져오기 (없으면 현재 프로젝트/uploads 사용)
    @Value("${upload.dir:${user.dir}/uploads}")
    private String uploadDir;

    // 서버 실행 시 딱 한 번 실행됨
    // 업로드 폴더가 없으면 자동으로 생성
    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Paths.get(uploadDir));
    }

    @Autowired
    private ProductsMapper productsMapper;

    @RequestMapping("/plist.prd")
    public String list(Model model,
                       @RequestParam(value = "whatColumn", required = false) String whatColumn,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page",defaultValue = "1") int page)
    {

        int limit = 5;
        int offset = (page -1) * limit;

        Map<String, Object> params = new HashMap<String,Object>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);

        // 전체 상품 조회 (검색 + 페이징)
        List<ProductsDto> lists = productsMapper.selectAll(params);
        // 전체 개수와 페이지 수 계산
        int totalCount = productsMapper.getCount(params);
        int totalPage = (int)Math.ceil((double) totalCount / limit);

        model.addAttribute("lists", lists);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("whatColumn", whatColumn);
        model.addAttribute("page", page);
        // 게시물 개수

        return "products/productList";
    }

    // 삽입 폼으로 이동
    @GetMapping(value="/pinsert.prd")
    public String insert(@ModelAttribute("pDto")ProductsDto pDto, HttpSession session){

        if(session.getAttribute("loginInfo") == null){ // 로그인(세션)이 없을 경우
            session.setAttribute("destination", "redirect:/pinsert.prd"); //
            return "redirect:/login.mb";
        }else{
            return "products/productInsert";
        }
    }

    // 삽입
    @PostMapping("pinsertProc.prd")
    public String insert(@Valid @ModelAttribute("pDto") ProductsDto productsDto,
                         BindingResult br,
                         Model model) {

        // 파일이 없으면 에러메세지 추가
        if(productsDto.getFile() == null || productsDto.getFile().isEmpty()) {
            br.rejectValue("file", "file.empty", "파일을 선택해주세요.");
        }

        // 유효성 검사 실패 시 다시 입력 폼으로 이동
        if(br.hasErrors()) {
            return "products/productInsert";
        }

        // 폴더 업로드
        MultipartFile file = productsDto.getFile(); // 업로드된 파일 객체
        String original = file.getOriginalFilename(); // 원본 파일명
        Path target = Paths.get(uploadDir,original); // 저장할 경로
        try {
            file.transferTo(target.toFile());        // 실제 파일 저장
        } catch (IOException e) {
            throw new RuntimeException(e);          // 예외 발생 시 런타임 에러
        }

        // DB에 저장할 파일명 세팅
        productsDto.setImage(original);
        // 상품 정보 DB에 삽입
        productsMapper.insertProduct(productsDto); // DB insert
        return "redirect:/plist.prd";
    }

    // 상세보기
    @RequestMapping(value = "/pcontent.prd")
    public String content(@RequestParam("num") int num,  Model model,
            @RequestParam(value = "whatColumn", required = false) String whatColumn,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page",defaultValue = "1") int page

    ){

        ProductsDto pDto = productsMapper.findByNum(num);
        model.addAttribute("keyword", keyword);
        model.addAttribute("whatColumn", whatColumn);
        model.addAttribute("page", page);
        model.addAttribute("pDto", pDto);

        return "products/productContent";
    }

    @GetMapping(value = "/images/{images}")
    @ResponseBody
    public ResponseEntity getimage(@PathVariable String images) throws MalformedURLException {
        Path file = Paths.get(uploadDir).resolve(images).normalize();
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + images + "\"")
                .body(resource);
    }

    // 수정폼으로 이동 (UpdateForm 이동)
    @GetMapping(value = "/pupdate.prd")
    public String updateForm(
            HttpSession session,
            @RequestParam("num") int num,  Model model,
            @RequestParam(value = "whatColumn", required = false) String whatColumn,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page",defaultValue = "1") int page
    ){


        if(session.getAttribute("loginInfo")==null){
            String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
            session.setAttribute("destination", "redirect:/pupdate.prd?num=" + num
                    + "&page=" + page
                    + "&whatColumn=" + whatColumn
                    + "&keyword=" + encodeKeyword);
            return "redirect:/login.mb";
        }else{
            ProductsDto pDto = productsMapper.findByNum(num);
            model.addAttribute("keyword", keyword);
            model.addAttribute("whatColumn", whatColumn);
            model.addAttribute("page", page);
            model.addAttribute("pDto", pDto);
            return "products/productUpdate";
        }

    }

    // 수정처리 (Update)
    @PostMapping(value = "/pupdateProc.prd")
    public String updateProc(@RequestParam("num") int num, HttpSession session,
            @ModelAttribute("pDto") @Valid ProductsDto pDto,BindingResult br, Model model,
            @RequestParam(value = "whatColumn", required = false) String whatColumn,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page",defaultValue = "1") int page
    ) {
        if(session.getAttribute("loginInfo") == null){
            String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
            session.setAttribute("destination", "redirect:/pupdate.prd?num=" + num
                    + "&page=" + page
                    + "&whatColumn=" + whatColumn
                    + "&keyword=" + encodeKeyword);
            return "redirect:/login.mb";
        }else{

            model.addAttribute("keyword", keyword);
            model.addAttribute("whatColumn", whatColumn);
            model.addAttribute("page", page);
            model.addAttribute("pDto", pDto);

            // 유효성 검정
            if(pDto.getFile() == null || pDto.getFile().isEmpty()) {
                br.rejectValue("file", "file.empty", "파일을 선택해주세요.");
            }

            if(br.hasErrors()) {
                return "products/productUpdate";
            }
            // 폴더 업로드
            MultipartFile file = pDto.getFile();
            String original = file.getOriginalFilename(); // 원본 파일명
            Path target = Paths.get(uploadDir,original); // 저장할 경로
            try {
                file.transferTo(target.toFile());        // 실제 파일 저장
            } catch (IOException e) {
                throw new RuntimeException(e);          // 예외 발생 시 런타임 에러
            }

            // DB에 저장할 파일명 세팅
            pDto.setImage(original);

            String resultPage = "";
            if (br.hasErrors()) {
                resultPage = "products/productUpdate";
            } else {
                productsMapper.updateProduct(pDto);
                String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
                resultPage = "redirect:/plist.prd?page=" + page + "&whatColumn=" + whatColumn + "&keyword=" + encodeKeyword;
            }
            return resultPage;
        }
    }

    @GetMapping(value="/pdelete.prd")
    public String deleteProc(
            HttpSession session,
            @RequestParam("num") int num, Model model,
            @RequestParam(value = "whatColumn", required = false) String whatColumn,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page",defaultValue = "1") int page
    ) {

        if (session.getAttribute("loginInfo") == null) {
            String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
            session.setAttribute("destination", "redirect:/pdelete.prd?num="+num
                    + "&page=" + page
                    + "&whatColumn=" + whatColumn
                    + "&keyword=" + encodeKeyword);
            return "redirect:/login.mb";
        } else {
            model.addAttribute("keyword", keyword);
            model.addAttribute("whatColumn", whatColumn);
            model.addAttribute("page", page);


            // ✅ 1. 삭제 전, 해당 상품의 이미지 파일명 조회 (이미지 파일 삭제)
            ProductsDto pDto = productsMapper.findByNum(num);
            if (pDto != null && pDto.getImage() != null) {
                Path filePath = Paths.get(uploadDir, pDto.getImage());
                File file = filePath.toFile(); // Path → File 객체 변환
//                    Files.deleteIfExists(file); // 1) 파일삭제 방법1
                    file.delete();         // 2) 파일삭제 방법2
                    System.out.println("이미지 삭제 성공 :" + file);
            }
            // ✅ 2. DB 레코드 삭제
            productsMapper.deleteProduct(num);

            // ✅ 3. 페이징 처리
            int limit = 5;
            int offset = (page - 1) * limit;

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("whatColumn", whatColumn);
            params.put("keyword", keyword);
            params.put("offset", offset);
            params.put("limit", limit);

            int totalCount = productsMapper.getCount(params);
            if (totalCount % limit == 0) {
                page = page - 1;
            }
            String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
            return "redirect:/plist.prd?page=" + page + "&whatColumn=" + whatColumn + "&keyword=" + encodeKeyword;
        }
    }

}
