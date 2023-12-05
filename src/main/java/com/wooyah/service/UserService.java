package com.wooyah.service;

import com.wooyah.dto.common.PaginationListDTO;
import com.wooyah.dto.user.UserDTO;
import com.wooyah.dto.user.request.UserLocationRequest;
import com.wooyah.dto.user.signup.UserSignUpDTO;
import com.wooyah.entity.Cart;
import com.wooyah.entity.CartUser;
import com.wooyah.entity.User;
import com.wooyah.exceptions.ExceptionMessage;
import com.wooyah.exceptions.NotFoundException;
import com.wooyah.repository.CartRepository;
import com.wooyah.repository.CartUserRepository;
import com.wooyah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartUserRepository cartUserRepository;
    public UserDTO.Phone getPhoneNum(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));

        return UserDTO.Phone.from(user);
    }

    @Transactional
    public void updateUserLocation(UserLocationRequest userLocation, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));

        userLocationUpdate(userLocation, user);
    }

    private static void userLocationUpdate(UserLocationRequest userLocation, User user) {
        user.setLatitude(userLocation.getLatitude());
        user.setLongitude(userLocation.getLongitude());
    }

    public PaginationListDTO<UserDTO.Detail> getMyCarts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));

        List<CartUser> carts = cartUserRepository.findALlByUser(user);

        List<Cart> myCarts = carts.stream()
                .filter(cartUser -> cartUser.getIsOwner().equals(Boolean.TRUE))
                .map(CartUser::getCart)
                .toList();

        List<UserDTO.Detail> response = myCarts.stream()
                .map(cart-> UserDTO.Detail.of(cart, cart.getCartUsers().size()))
                .toList();

        return PaginationListDTO.<UserDTO.Detail>builder()
                .count(myCarts.size())
                .data(response)
                .build();
    }

    //이메일 db 조회후 처리하기
    @Transactional
    public String emailcheck(UserSignUpDTO userSignUpDto) {

        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            return "이메일 정보가 이미 있습니다.";
        }
        else {
            User user = User.builder()
                    .email(userSignUpDto.getEmail())
                    .nickname(userSignUpDto.getNickname())
                    .phone(userSignUpDto.getPhone())
                    .deviceNumber(userSignUpDto.getDevice_number())
                    .location(userSignUpDto.getLocation())
                    .build();

            userRepository.save(user);
            return "회원가입 완료";
        }
    }
}
