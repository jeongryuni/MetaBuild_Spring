package com.example.Ex02.repository;

import com.example.Ex02.entity.EntityTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EntityRepositoryTest {

    @Autowired
    EntityRepository entityRepository;

    @Test
    @DisplayName("entity 테스트")
    public void entitySave(){
        EntityTest etest = new EntityTest();
        etest.setNum(100);
        etest.setName("김연아");
        etest.setAddr("경기");
        etest.setAge(20);

        entityRepository.save(etest);

    }

    @Test
    @DisplayName("10개 레코드 삽입")
    public void e_testSave(){
        String[] irum = {"윤아", "아이유", "웬디", "민호"};
        String[] addr = {"서울", "제주", "부산"};
        int[] age = {10, 20, 30, 40};

        for(int i=1; i<=10; i++){
            EntityTest etest = new EntityTest();
            etest.setNum(i);
            etest.setName(irum[i % irum.length]); // 1 2 3 0 1 2 나눠서 나머지 결과값으로 반복작업
            etest.setAddr(addr[i % addr.length]);
            etest.setAge(age[i % age.length]);
            entityRepository.save(etest);
        }
    }

    @Test
    @DisplayName("전체 레코드 조회")
    public void findAllTest(){
        List<EntityTest> elists = entityRepository.findAll(); //전체 레코드 조회 : List 리턴
        for(EntityTest entity :elists){
/*            System.out.println(entity.getNum());
            System.out.println(entity.getName());
            System.out.println(entity.getAddr());
            System.out.println(entity.getAge());*/
            System.out.println(entity.toString()); // 메서드를 오버라이딩 하지않을시 => 주소출력, dto에서 오버라이딩
            System.out.println("================");
        }
    }
    @Test
    @DisplayName("특정 레코드 조회")
    public void findName(){
        List<EntityTest> elists = entityRepository.findByName("윤아");
        //select * from e_test where irun = "윤아"
        for(EntityTest entity : elists){
            System.out.println(entity.toString());
        }
    }

    @Test
    @DisplayName("특정 레코드 조회")
    public void findAge(){
        List<EntityTest> elists = entityRepository.findByAge(30);
        for(EntityTest entity : elists){
            System.out.println(entity.toString());
        }
    }

    @Test
    @DisplayName("특정 레코드 조회")
    public void findAgeOver(){
        List<EntityTest> elists = entityRepository.findByAgeGreaterThan(30);
        for(EntityTest entity : elists){
            System.out.println(entity.toString());
        }
    }

    @Test
    @DisplayName("특정 레코드 조회")
    public void findAgeEqual(){
        List<EntityTest> elists = entityRepository.findByAgeGreaterThanEqual(30);
        for(EntityTest entity : elists){
            System.out.println(entity.toString());
        }
    }
}
