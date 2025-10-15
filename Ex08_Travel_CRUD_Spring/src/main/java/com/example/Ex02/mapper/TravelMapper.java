package com.example.Ex02.mapper;

import com.example.Ex02.dto.TravelDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TravelMapper {
    void insertTravel(TravelDto tDto);
    List<TravelDto> selectAll(Map<String, Object> map);
    /*List<TravelDto> selectAll();*/
    TravelDto findByNum(int num);
    void updateTravel(TravelDto tDto);
    void deleteTravel(int num);

    int getCount(Map<String, Object> map);
}
