package com.example.Ex02.repository;

import ch.qos.logback.core.util.COWArrayList;
import com.example.Ex02.entity.CompanyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CompanyRepositoryTest {

    @Autowired
    CompanyRepository companyRepository;

    @Test
    @DisplayName("한줄 레코드 삽입")
    public void entitySave(){
        CompanyEntity cEntity = new CompanyEntity();
        cEntity.setId(1);
        cEntity.setName("이름1");
        cEntity.setCompany("회사");
        cEntity.setPart("파트1");
        cEntity.setSalary(100);

        companyRepository.save(cEntity);
        System.out.println("✅ 저장 완료: " + cEntity.getId());

    }

    @Test
    @DisplayName("여러줄 레코드 삽입")
    public void multiSave(){

        String[] name = {"정국","수지","태형","원영"};
        String[] company = {"삼성","메타빌드","현대"};
        String[] part = {"대리","사원","과장"};
        int[] salary = {300,200,100,400};

        for(int i=0; i<=10; i++){
            CompanyEntity cEntity = new CompanyEntity();
            cEntity.setId(i);
            cEntity.setName(name[i%name.length]);
            cEntity.setCompany(company[i%company.length]);
            cEntity.setPart(part[i%part.length]);
            cEntity.setSalary(salary[i%salary.length]);

            companyRepository.save(cEntity);
        }
    }

    @Test
    @DisplayName("전체 레코드 조회")
    public void findAllTest(){
        List<CompanyEntity> lists = companyRepository.findAll();

        for(CompanyEntity cEntity : lists){
            System.out.println(cEntity.toString());
        }
    }

    @Test
    @DisplayName("급여가 300이상인 레코드 조회")
    public void findSalaryFind(){
        List<CompanyEntity> lists = companyRepository.findBySalaryGreaterThanEqual(300);

        for(CompanyEntity cEntity : lists){
            System.out.println(cEntity.toString());
        }
    }

    @Test
    @DisplayName("아이디 조회")
    public void findIdFind(){

        Optional<CompanyEntity> cEntity = companyRepository.findById(5);
            System.out.println(cEntity.toString());

    }

    @Test
    @DisplayName("이름에 '수'가 포함된 이름 조회")
    public void findNameFind(){
        List<CompanyEntity> lists = companyRepository.findByNameContaining("수");

        for(CompanyEntity cEntity : lists) {
            System.out.println(cEntity.toString());
        }
    }

    @Test
    @DisplayName("이름에 '수'가 포함된 이름 조회 & 회사명 내림차순 조회")
    public void findNameAndCompanyFind(){
        List<CompanyEntity> lists = companyRepository.findByNameContainingOrderByCompanyDesc("수");

        for(CompanyEntity cEntity : lists) {
            System.out.println(cEntity.toString());
        }
    }

    //쿼리 어노테이션
    @Test
    @DisplayName("Query Annotation으로 전체레코드 조회")
    public void allSelect(){
        List<CompanyEntity> lists = companyRepository.findAllQuery();
        for(CompanyEntity cEntity : lists) {
            System.out.println(cEntity.toString());
        }
    }

    @Test
    @DisplayName("전체 레코드 개수조회")
    public void countCompany(){
        int count = companyRepository.countCompany();
        System.out.println("➡️count :" + count);
    }

    @Test
    @DisplayName("이름이 '수지'인 레코드 조회")
    public void findByNameSuji(){
        List<CompanyEntity> lists = companyRepository.findByNameSuji("수지");
        for(CompanyEntity cEntity : lists) {
            System.out.println(cEntity.toString());
        }
    }

    @Test
    @DisplayName("이름에 '수'가 포함된 레코드 조회")
    public void findByNameSujiContainQuery(){
        List<CompanyEntity> lists = companyRepository.findNameContainSu("수");
        for(CompanyEntity cEntity : lists) {
            System.out.println(cEntity.toString());
        }
    }
}
