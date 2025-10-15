package com.example.Ex02.repository;

import com.example.Ex02.entity.ItemEntity;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    List<ItemEntity> findByItemNm(String ss);

    List<ItemEntity> findByPriceLessThanOrderByPriceDesc(int i);

    List<ItemEntity> findByPriceGreaterThanOrderByPriceAsc(int i);

    // 쿼리 어노테이션
    // 5. 상세설명에 '어'들어간 레코드 찾기
    @Query(value = "select i from ItemEntity i where i.itemDetail like %:itemDetail%")
    List<ItemEntity> findByItemDetail(@Param("itemDetail")String itemDetail);

    // 6. 상세설명에 '어'포함 & price가 300이상인 레코드 조회
    @Query(value = "select i from ItemEntity i where i.itemDetail like %:itemDetail% and i.price >= :price")
    List<ItemEntity> findByItemDetailPrice(@Param("itemDetail, price") String itemDetail, int price);


    // 8. 배 삭제
    @Modifying
    @Transactional
    @Query(value = "delete from item_entity where item_name = :itemname", nativeQuery = true)
    /*@Query(value = "delete from itemEntity where itemName = :itemname")*/
    void deleteItemName(@Param("itemname") String itemname);
}

