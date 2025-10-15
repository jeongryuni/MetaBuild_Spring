package com.example.Ex02.repository;

import com.example.Ex02.entity.ItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("1. item레코드 삽입")
    public void Save(){
        ItemEntity itemEntity = new ItemEntity();
        // itemEntity.setId(1L);
        itemEntity.setItemNm("아이템명");
        itemEntity.setPrice(1000);
        itemEntity.setItemDetail("아이템설명");
        itemEntity.setRegTime(LocalDateTime.now());
        itemRepository.save(itemEntity);
    }
    @Test
    @DisplayName("2. item레코드 반복 삽입")
    public void MultiSave(){
        String[] fruit = {"사과", "배", "오렌지"};
        String[] description = {"달아요", "맛있어요", "맛없어요", "떫어요"};
        int[] price = {111, 222, 333, 444, 555};

        for(int i=1; i<=10; i++){
        ItemEntity itemEntity = new ItemEntity();
            itemEntity.setItemNm(fruit[i % fruit.length]);
            itemEntity.setPrice(price[i % price.length]);
            itemEntity.setItemDetail(description[i % description.length]);
            itemEntity.setRegTime(LocalDateTime.now());
            itemRepository.save(itemEntity);
        }

    }

    @Test
    @DisplayName("3. item레코드 모든 상품 조회")
    public void AllSelect(){
       List<ItemEntity>lists = itemRepository.findAll();
       for (ItemEntity item:lists){
           System.out.println(item);
       }
    }

    @Test
    @DisplayName("4. 오렌지 조회")
    public void getOrange(){
        List<ItemEntity>lists = itemRepository.findByItemNm("오렌지");
        for (ItemEntity item:lists){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("5. id 조회")
    // PK가 num인 경우 findById()와 findByNum()은 결과가 동일하다. 하지만 findById()는 PK 전용
    public void getId(){
        Optional<ItemEntity> o = itemRepository.findById(50L);
        System.out.println("o" + o);
    }

    @Test
    @DisplayName("6. price조회")
    // price 300보다 작은 레코드를 가격기준 내림차순 정렬 조회 출력
    // Query method
    public void getPrice(){
        List<ItemEntity>lists = itemRepository.findByPriceLessThanOrderByPriceDesc(300);
        for (ItemEntity item:lists){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("7. price조회")
    // price 300큰 레코드를 가격기준 오름차순 정렬 조회 출력
    // Query method
    public void getPrice2(){
        List<ItemEntity>lists = itemRepository.findByPriceGreaterThanOrderByPriceAsc(300);
        for (ItemEntity item:lists){
            System.out.println(item);
        }
    }

    // 쿼리 어노터이션
    // 상세설명에 '어' 포함된 레코드 조회
    @Test
    @DisplayName("상세설명에 '어' 포함된 레코드 조회")
    public void ItemDetailFind(){
        List<ItemEntity>lists = itemRepository.findByItemDetail("어");
        for (ItemEntity item:lists){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("상세설명에 '어' 포함 & 가격이 300이상 레코드 조회")
    public void ItemDetailPriceFind(){
        List<ItemEntity>lists = itemRepository.findByItemDetailPrice("어", 300);
        for (ItemEntity item:lists){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("7. 레코드 삭제")
    public void deleteId(){
        itemRepository.deleteById(1L);
        }

    @Test
    @DisplayName("itemName=배 레코드 삭제")
    public void deleteItemName(){
        itemRepository.deleteItemName("배");
    }
}
