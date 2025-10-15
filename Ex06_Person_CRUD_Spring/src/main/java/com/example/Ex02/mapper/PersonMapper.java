package com.example.Ex02.mapper;

import com.example.Ex02.dto.PersonDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*인터페이스 : 정의 되지 않은 메서드 작성*/

@Mapper
public interface PersonMapper {
    void insertPerson(PersonDto person);
    List<PersonDto> selectAll();
    PersonDto findByNum(int x);
    int updatePerson(PersonDto per);
    int deletePerson(int num);

}
