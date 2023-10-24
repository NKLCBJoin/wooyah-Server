package com.wooyah.service;

import com.wooyah.dto.cart.CartDTO;
import com.wooyah.entity.Cart;
import com.wooyah.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;

    public CartDTO.Detail getDetail(Long cartId){
        Cart findCart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("cartId에 해당하는 cart가 존재하지 않습니다."));

        return CartDTO.Detail.entityToDto(findCart);
    }
}
