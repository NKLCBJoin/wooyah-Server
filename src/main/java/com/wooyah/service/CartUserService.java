package com.wooyah.service;


import com.wooyah.dto.cartuser.CartUserDTO;
import com.wooyah.dto.product.ProductDTO;
import com.wooyah.entity.Cart;
import com.wooyah.entity.CartProduct;
import com.wooyah.entity.CartUser;
import com.wooyah.entity.User;
import com.wooyah.entity.enums.CartStatus;
import com.wooyah.entity.enums.CartUserStatus;
import com.wooyah.exceptions.ExceptionMessage;
import com.wooyah.exceptions.NotFoundException;
import com.wooyah.repository.CartRepository;
import com.wooyah.repository.CartUserRepository;
import com.wooyah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartUserService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartUserRepository cartUserRepository;


    public void saveCartUser(Long cartId, String userEmail) {

        //cart_id의 CartStatus를 확인하여 INPROGRESS인 경우에만 통과
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 id가 잘못 됐습니다"));

        // 장바구니 상테 체크
        if (CartStatus.INPROGRESS != cart.getStatus()) {
            throw new IllegalStateException("장바구니 상태가 progress가 아닙니다.");
        }

        //participantNumber의 값이 cart_user의 isOwner을 포함하여 CartUserStatus 상태가 approved된 사람의 수보다 이하인 경우에만 통과
        long approvedParticipants = cart.getCartUsers().stream()
                .filter(cartUser -> CartUserStatus.APPROVED.equals(cartUser.getStatus()))
                .count();

        if (cart.getParticipantNumber() <= approvedParticipants) {
            throw new IllegalStateException("참가자가 초과.");
        }

        //주어진 이메일로 User 엔티티를 찾음
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일 사용자 찾을 수 없음"));

        //CartUser에 입력받은 cart_id, user_id 등을 입력하여 JPA를 이용하여 DB에 저장
        CartUser cartUser = CartUser.builder()
                .user(user)
                .cart(cart)
                .isOwner(false) // 기본값으로 설정, 필요에 따라 변경
                .status(CartUserStatus.PENDING) // 기본값으로 설정, 필요에 따라 변경
                .build();

        //이렇게 하는게 맞나..?
        cartUser.setUser(user);
        cartUser.setCart(cart);


        cartUserRepository.save(cartUser);
    }


    public CartUserDTO getCartUserStatus(Long cartId, String userEmail) {

        //아니면 cartId에 cartUser List 얻어서 그 리스트에서 해당하는 user_id를 검색?
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.CART_NOT_FOUND));

        Optional<CartProduct> productDetails = cart.getCartProducts().stream()
                .filter(id -> id.equals(cartId))
                .findFirst();


        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일 사용자 찾을 수 없음"));

        //cart_id와 user_id로 CartUser 엔티티를 찾기
        CartUser cartUser = cartUserRepository.findByCartIdAndUserId(cartId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카트 및 사용자에 대한 CartUser를 찾을 수 없음"));

        return CartUserDTO.builder()
                .status(cartUser.getStatus())
                .build();

    }

}
