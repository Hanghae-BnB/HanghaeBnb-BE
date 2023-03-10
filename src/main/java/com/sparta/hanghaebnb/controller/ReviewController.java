package com.sparta.hanghaebnb.controller;

import com.sparta.hanghaebnb.dto.response.MessageResponseDto;
import com.sparta.hanghaebnb.dto.request.ReviewRequestDto;
import com.sparta.hanghaebnb.response.ApiDocumentResponse;
import com.sparta.hanghaebnb.security.UserDetailsImpl;
import com.sparta.hanghaebnb.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@ApiDocumentResponse
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "해당 숙박지에 대한 리뷰 작성 요청", description = "해당 숙박지에 대한 리뷰 작성", tags = {"review"})
    @PostMapping("/houses/{house-id}/reviews")
    public MessageResponseDto createReview(@PathVariable("house-id") Long houseId, @RequestBody ReviewRequestDto reviewRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.createReview(houseId, reviewRequestDto, userDetails.getUser());
    }

    @Operation(summary = "해당 숙박지에 대한 리뷰 삭제 요청", description = "해당 숙박지에 대한 리뷰 삭제", tags = {"review"})
    @DeleteMapping("/houses/{house-id}/reviews")
    public MessageResponseDto deleteReview(@PathVariable("house-id") Long houseId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.deleteReview(houseId, userDetails.getUser());
    }



}
