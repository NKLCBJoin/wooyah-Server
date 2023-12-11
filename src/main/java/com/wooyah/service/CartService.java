package com.wooyah.service;

import com.wooyah.dto.cart.CartDTO;
import com.wooyah.dto.cart.CartHomeDTO;
import com.wooyah.dto.cart.CartWriteDTO;
import com.wooyah.dto.common.PaginationListDTO;
import com.wooyah.entity.*;
import com.wooyah.entity.enums.CartStatus;
import com.wooyah.entity.enums.CartUserStatus;
import com.wooyah.exceptions.BadRequestException;
import com.wooyah.exceptions.ExceptionMessage;
import com.wooyah.exceptions.NotFoundException;
import com.wooyah.jwt.JwtAuthenticationProcessingFilter;
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

    private final ProductService productService;

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


    public PaginationListDTO<CartDTO.Near> getAllCarts() {
        List<Cart> allCarts = cartRepository.findAll();

        List<CartDTO.Near> response = allCarts.stream()
                .map(CartDTO.Near::from)
                .toList();

        return PaginationListDTO.<CartDTO.Near>builder()
                .count(response.size())
                .data(response)
                .build();
    }

    //추가 코드
    public CartHomeDTO.Detail getHomeDetail(){
        List<Cart> carts = cartRepository.findAll();

        return CartHomeDTO.Detail.from(carts);
    }

    //카트 생성
    @Transactional
    public void saveCart(CartWriteDTO cartWriteDTO, Long userId) {

        User user = userRepository.findById(userId).get();

        //cart 생성
        Cart cart = Cart.builder()
                .shoppingLocation(cartWriteDTO.getLocation())
                .participantNumber(cartWriteDTO.getParticipantNumber())
                .latitude(cartWriteDTO.getLatitude())
                .longitude(cartWriteDTO.getLongitude())
                .status(CartStatus.INPROGRESS)
                .build();

        //product db에 저장
        List<Product> products = productService.saveProducts(cartWriteDTO.getProducts());

        // cart에 있는 cartProducts에 추가
        for (Product product : products) {
            CartProduct cartProduct = CartProduct.builder()
                    .product(product)
                    .cart(cart)
                    .build();

            cart.addCartProduct(cartProduct);
        }

        // 사용자와 장바구니 연결
        CartUser cartUser = CartUser.builder()
                .user(user)
                .cart(cart)
                .isOwner(true)
                .status(CartUserStatus.APPROVED)
                .build();

        cart.addCartUser(cartUser);

        // 장바구니 저장
        cartRepository.save(cart);
    }
}
