package com.example.Ex02.service;

import com.example.Ex02.dto.FileInfo;
import com.example.Ex02.mapper.FileMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    // user.dir 유저 현재 프로젝트
    @Value("${upload.dir:${user.dir}/uploads}")
    private String uploadDir; // c:Ex09/uploads

    @PostConstruct
    public void init() throws IOException {
        System.out.println("init");

        // 폴더 자동생성
        Files.createDirectories(Paths.get(uploadDir));
    }

    public void uploadFile(FileInfo fileInfo) throws IOException {
        String name = fileInfo.getName();
        MultipartFile file = fileInfo.getFile();
        String name2 = file.getName();
        String original = file.getOriginalFilename();

        System.out.println("name :" + name); // 사용자가 작성한 이름
        System.out.println("name2 :" + name2); // HTML 폼에서 <input type="file" name="file"> 의 name="file" 속성
        System.out.println("original :" + original); // 사용자가 업로드한 파일의 원래 파일명

        Path target = Paths.get(uploadDir, original); // 올릴경로, 올릴파일
        file.transferTo(target.toFile()); // 전송 (폴더 업로드)

        fileInfo.setFilename(original);
        fileInfo.setFileSize((int)file.getSize());

        fileMapper.saveFileInfo(fileInfo);

    }

    public List<FileInfo> getAllFiles() {
        return fileMapper.getAllFiles();
    }
}
