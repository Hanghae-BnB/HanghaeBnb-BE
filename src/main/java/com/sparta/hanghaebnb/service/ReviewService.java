package com.sparta.hanghaebnb.service;

import com.sparta.hanghaebnb.dto.MessageResponseDto;
import com.sparta.hanghaebnb.dto.ReviewRequestDto;
import com.sparta.hanghaebnb.entity.House;
import com.sparta.hanghaebnb.entity.Review;
import com.sparta.hanghaebnb.entity.User;
import com.sparta.hanghaebnb.repository.HouseRepository;
import com.sparta.hanghaebnb.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final HouseRepository houseRepository;
    private final ReviewRepository reviewRepository;

    // 리뷰 생성
    @Transactional
    public ResponseEntity<MessageResponseDto> createReview(Long id, ReviewRequestDto reviewRequestDto, User user) {
        // 해당 게시글이 있는지 확인
        Optional<House> house = houseRepository.findById(id);
        if (house.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        } else {
            Review review = new Review(reviewRequestDto, user, house.get());
            reviewRepository.save(review);
            return ResponseEntity.ok().body(MessageResponseDto.of("리뷰 작성 성공!", HttpStatus.OK));

        }

    }
}