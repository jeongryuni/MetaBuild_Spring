package com.example.Ex02.service;

/*controller - BookService - repository :중간 연결다리*/

import com.example.Ex02.bean.BookBean;
import com.example.Ex02.entity.BookEntity;
import com.example.Ex02.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    // ModelMapper : Bean ↔ Entity 자동 변환 도구
    private static ModelMapper modelMapper = new ModelMapper();

    // 1. 도서 총 개수 조회
    public long count() {
        return bookRepository.count();
    }

    // 2. 도서 목록 조회 (검색 + 페이징)
    // Pageable : 페이징 정보와 정렬 기준 지정 (no 기준 내림차순)
    public Page<BookEntity> getBookEntity(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("no").descending());
        if (keyword == null || keyword.isEmpty()){
            return bookRepository.findAll(pageable); // 4페이지면 9개 건너뛰고 3개 조회
        }else{
            return bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword, pageable);
        }
    }

    // 3.  도서 저장 (등록 또는 수정
    public void saveBook(BookEntity  bookEntity){
        bookRepository.save(bookEntity);
    }

    // 4. Bean → Entity 변환
    public BookEntity beanToEntity(BookBean bookBean) {
        BookEntity bookEntity= modelMapper.map(bookBean, BookEntity.class);
        return bookEntity;
    }

    // 5. 도서 상세보기
    public BookEntity getBookByNo(int no) {
        return bookRepository.findById(no).orElse(null);
    }

    // 6. 도서 수정
    public void updateBook(BookEntity bookEntity) {
        bookRepository.save(bookEntity);
    }

    // 7. 도서 삭제
    public void deleteBook(int no) {
        bookRepository.deleteById(no);
    }
}

