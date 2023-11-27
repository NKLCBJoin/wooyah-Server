package com.wooyah.service;

import com.wooyah.dto.cart.CartDTO;
import com.wooyah.dto.common.PaginationListDTO;
import com.wooyah.entity.Cart;
import com.wooyah.entity.CartUser;
import com.wooyah.entity.User;
import com.wooyah.entity.enums.CartStatus;
import com.wooyah.exceptions.BadRequestException;
import com.wooyah.exceptions.ExceptionMessage;
import com.wooyah.exceptions.NotFoundException;
import com.wooyah.repository.CartRepository;
import com.wooyah.repository.CartUserRepository;
import com.wooyah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CartDTO.Detail getDetail(Long cartId){
        Cart findCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.CART_NOT_FOUND));

        return CartDTO.Detail.from(findCart);
    }

    public PaginationListDTO<CartDTO.Near> getNearCarts(Long userId, int zoom) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));

        List<Cart> findCarts = cartRepository.findCartsNearestTo(user.getLatitude(), user.getLongitude(), zoom*5);

        List<CartDTO.Near> nearCarts = getNearsCarts(findCarts);

        return PaginationListDTO.<CartDTO.Near>builder()
                .count(nearCarts.size())
                .data(nearCarts)
                .build();
    }

    public PaginationListDTO<CartDTO.Near> getNearCarts(BigDecimal latitude, BigDecimal longitude, int zoom) {

        List<Cart> findCarts = cartRepository.findCartsNearestTo(latitude, longitude, zoom*5);

        List<CartDTO.Near> nearCarts = getNearsCarts(findCarts);

        return PaginationListDTO.<CartDTO.Near>builder()
                .count(nearCarts.size())
                .data(nearCarts)
                .build();
    }

    @Transactional
    public void deleteCart(Long userId, Long cartId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.CART_NOT_FOUND));

        List<CartUser> cartUsers = cart.getCartUsers();

        CartUser cartUser = cartUsers.stream()
                .filter(cu -> cu.getCart().equals(cart))
                .filter(cu -> cu.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.CART_USER_NOT_FOUND));

        if(cartUser.getIsOwner()){
            cartRepository.deleteById(cartId);
        }else{
            throw new BadRequestException(ExceptionMessage.USER_DOES_NOT_OWN_CART);
        }
    }

    private static List<CartDTO.Near> getNearsCarts(List<Cart> findCarts) {
        return findCarts.stream()
                .filter(cart -> cart.getStatus().equals(CartStatus.INPROGRESS))
                .map(CartDTO.Near::from)
                .toList();
    }




}
