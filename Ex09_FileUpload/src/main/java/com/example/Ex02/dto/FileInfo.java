package com.example.Ex02.dto;


import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class FileInfo {
    private int id;

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    private String filename;
    private  int fileSize;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    private MultipartFile file; // 파일(데이터) 생성  ==> 업로드된 파일 자체가 들어오는 필드

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
