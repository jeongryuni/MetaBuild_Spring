package com.example.Ex02.mapper;

import com.example.Ex02.dto.BreadDto;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BreadMapper {
    List<BreadDto> breadSelectAll(Map<String, Object> params);

    void insertBread(BreadDto bDto);

    BreadDto selectById(int id);

    void updateBread(@Valid BreadDto bDto);

    int getCount(Map<String, Object> params);

    void deleteBread(int id);

    int selectCountCode(String code);
}

