package com.example.Ex02.service;

import com.example.Ex02.bean.MovieBean;
import com.example.Ex02.entity.MovieEntity;
import com.example.Ex02.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public static ModelMapper modelMapper = new ModelMapper();

    // 1. 총 개수 조회
    public long count() {
       return movieRepository.count();
    }

    // 2. 조회
    public Page<MovieEntity> getMovieEntity(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("num").descending());
        if(keyword==null || keyword.isEmpty()){
            return movieRepository.findAll(pageable);
        }
        return movieRepository.findByNameContainingOrGenreContaining(keyword, keyword, pageable);
    }

    // 3. Bean → Entity 변환
    public MovieEntity beanToEntity(MovieBean movieBean) {
        MovieEntity movieEntity = modelMapper.map(movieBean, MovieEntity.class);
        return movieEntity;
    }

    // 4. 영화 등록(insert)
    public void saveMovie(MovieEntity movieEntity) {
        movieRepository.save(movieEntity);
    }

    // 5. 영화 상세보기(select getByNum)
    public MovieEntity getMovieByNum(int num) {
        return movieRepository.findById(num).orElse(null);
    }

    // 6. 수정처리(update)
    public void updateMovie(MovieEntity movieEntity) {
         movieRepository.save(movieEntity);
    }

    // 7. 다중삭제(체크박스)
    public void deleteMovie(int num) {
        movieRepository.deleteById(num);
    }
}
