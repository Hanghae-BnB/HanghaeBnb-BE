package com.sparta.hanghaebnb.service;

import com.sparta.hanghaebnb.dto.MessageResponseDto;
import com.sparta.hanghaebnb.entity.House;
import com.sparta.hanghaebnb.entity.User;
import com.sparta.hanghaebnb.entity.WishList;
import com.sparta.hanghaebnb.entity.WishListAndHouse;
import com.sparta.hanghaebnb.repository.HouseRepository;
import com.sparta.hanghaebnb.repository.UserRepository;
import com.sparta.hanghaebnb.repository.WishListAndHouseRepository;
import com.sparta.hanghaebnb.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final WishListAndHouseRepository wishListAndHouseRepository;

    @Transactional
    public MessageResponseDto addWishList(Long id, User user) {
        // 1) 위시리스트에 넣으려는 숙소가 없는 경우
        Optional<House> house = houseRepository.findById(id);
        if (house.isEmpty()) {
            throw new IllegalArgumentException("해당 숙소가 존재하지 않습니다.");
        }

        Optional<WishList> wishList = wishListRepository.findByUser(user);
        if(wishList.isEmpty()) {
            wishList = Optional.of(WishList.of(user));
            wishListRepository.save(wishList.get());
        }

        // 2) 이미 위시리스트에 있는 경우
        Optional< WishListAndHouse> wishListAndHouse = wishListAndHouseRepository.findByWishListAndHouse(wishList.get(), house.get());
        if (wishListAndHouse.isPresent()) {
            throw new IllegalArgumentException("이미 위시리스트에 추가하셨습니다.");
        }

        wishListAndHouseRepository.save(new WishListAndHouse(wishList.get(), house.get()));
        return new MessageResponseDto("위시리스트 작성 성공!", HttpStatus.OK);

    }

    @Transactional
    public MessageResponseDto deleteWishList(Long id, User user) {

        Optional<House> house = houseRepository.findById(id);
        if (house.isEmpty()) {
            throw new IllegalArgumentException("해당 숙소가 존재하지 않습니다.");
        }

        Optional<WishList> wishList = wishListRepository.findByUser(user);
        if (wishList.isEmpty()) {
            throw new IllegalArgumentException("해당 위시리스트가 존재하지 않습니다.");
        }

        Optional< WishListAndHouse> wishListAndHouse = wishListAndHouseRepository.findByWishListAndHouse(wishList.get(), house.get());
        if (wishListAndHouse.isEmpty()) {
            throw new IllegalArgumentException("추가된 항목이 없습니다.");
        }

        wishListAndHouseRepository.delete(wishListAndHouse.get());
        return new MessageResponseDto("위시리스트 삭제 성공!", HttpStatus.OK);

    }



}