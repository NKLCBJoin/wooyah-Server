package com.wooyah.service;

import com.wooyah.dto.cart.CartDTO;
import com.wooyah.dto.common.PaginationListDTO;
import com.wooyah.entity.Cart;
import com.wooyah.entity.User;
import com.wooyah.exceptions.ExceptionMessage;
import com.wooyah.repository.CartRepository;
import com.wooyah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    public CartDTO.Detail getDetail(Long cartId){
        Cart findCart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.CART_NOT_FOUND.getMessage()));

        return CartDTO.Detail.from(findCart);
    }

    public PaginationListDTO<CartDTO.Near> getNearCarts(Long userId, int zoom) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.USER_NOT_FOUND.getMessage()));
        List<Cart> findCarts = cartRepository.findCartsNearestTo(user.getLongitude(), user.getLatitude(), zoom*5);

        List<CartDTO.Near> nearCarts = findCarts.stream()
                .map(CartDTO.Near::from)
                .toList();

        return PaginationListDTO.<CartDTO.Near>builder()
                .count(nearCarts.size())
                .data(nearCarts)
                .build();
    }


}
