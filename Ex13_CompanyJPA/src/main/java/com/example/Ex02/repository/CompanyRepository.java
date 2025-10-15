package com.example.Ex02.repository;


import com.example.Ex02.entity.CompanyEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {

    List<CompanyEntity> findBySalaryGreaterThanEqual(int i);

    List<CompanyEntity> findByNameContaining(String 수);

    List<CompanyEntity> findByNameContainingOrderByCompanyDesc(String 수);

    // nativeQuery = true : 테이블명 작성
    // nativeQuery = false : 클래스명 작성

    // 1. 전체 조회
    /*@Query(value = "select * from company_entity", nativeQuery = true)*/
    @Query(value = "select i from CompanyEntity i", nativeQuery = false)
    List<CompanyEntity> findAllQuery();

    // 2. 레코드 개수 조회
    @Query("select count(company) from CompanyEntity company")
    int countCompany();

    // 3. 이름이 '수지'인 사람 조회
    @Query("SELECT i FROM CompanyEntity i WHERE i.name = :name")
    List<CompanyEntity> findByNameSuji(@Param("name") String name);

    // 4. 이름에 '수'가 포함된 사람조회 & 회사이름 내림차순
    @Query("SELECT i FROM CompanyEntity i WHERE i.name LIKE %:name% order by company desc")
    List<CompanyEntity> findNameContainSu(@Param("name") String name);

}
