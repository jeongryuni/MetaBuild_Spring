package com.example.Ex02.mapper;

import com.example.Ex02.dto.FileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    void saveFileInfo(FileInfo fileInfo);
    List<FileInfo> getAllFiles();
}
