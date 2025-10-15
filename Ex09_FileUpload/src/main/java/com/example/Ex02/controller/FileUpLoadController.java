package com.example.Ex02.controller;

import com.example.Ex02.dto.FileInfo;
import com.example.Ex02.service.FileService;
import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class FileUpLoadController {

    @Autowired
    FileService fileService;
    public static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @GetMapping(value = "/form")
    public String form(Model model){
        model.addAttribute("fileInfo", new FileInfo());
        return "form";
    }

    // 업로드 post처리
    @PostMapping("/upload")
    public String upload(@Valid FileInfo fileInfo, BindingResult br, Model model){

        // 파일 유효성 검사 ==> 파일은 수동으로 체크 필요
        if(fileInfo.getFile()==null || fileInfo.getFile().isEmpty()){
            br.rejectValue("file", "file.empty", "파일을 선택하세요");
        }

        if(br.hasErrors()){
            model.addAttribute("fileInfo", fileInfo);
            model.addAttribute("message", br.getAllErrors().get(0).getDefaultMessage());
            return "form";
        }
        // controller - service - interface - mapper

        // 파일 저장 시도
        try {
            fileService.uploadFile(fileInfo);
            model.addAttribute("message", "업로드 성공" + fileInfo.getFilename());
        }catch (IOException e){
            model.addAttribute("message", "업로드 실패");
        }

        return "form";
    }

    // 이미지 띄우기 a태그 --> 전체 이미지 보기
    @GetMapping(value = "/images")
    public String images(Model model){
        List<FileInfo> lists = fileService.getAllFiles();
        System.out.println("lists.size()" + lists.size()); // 넣은 값 개수 확인

        model.addAttribute("lists",lists);
        return "list";
    }

    // 이미지 띄우기 get방식 요청
    // {filename:.+} → 확장자(.) 포함된 파일명도 PathVariable로 받기 가능
    @GetMapping(value="/images/{filename:.+}")
    public ResponseEntity images(@PathVariable String filename) throws MalformedURLException {

        Path file = Paths.get(UPLOAD_DIR).resolve(filename).normalize(); // UPLOAD_DIR 경로에 filename에 접근
        Resource resource = new UrlResource(file.toUri());
        // inline → 브라우저에서 바로 열림
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);

    }
}
