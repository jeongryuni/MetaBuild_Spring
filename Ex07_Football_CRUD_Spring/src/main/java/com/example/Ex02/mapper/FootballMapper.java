package com.example.Ex02.mapper;

import com.example.Ex02.dto.FootballDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FootballMapper {
    void insertFootball(FootballDto fDto); // 입력한 한 묶음 넘기기
    /*List<FootballDto> selectAll();*/  // <전체조회> -- List<FootballDto> 리스트 리턴
    FootballDto findByNum(int num); // <상세조회> -- FootballDto객체 리턴
    void updateFootball(FootballDto fDto);
    void deleteFootball(int num);

    List<FootballDto> selectAll(Map<String, Object> map);
    int getCount(Map<String, Object> map);
}
