package com.wooyah.service;

import com.wooyah.IntegrationTestSupport;
import com.wooyah.dto.cart.CartDTO;
import com.wooyah.dto.common.PaginationListDTO;
import com.wooyah.entity.*;
import com.wooyah.repository.CartRepository;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class CartServiceTest extends IntegrationTestSupport {

    @Autowired
    private CartService cartService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    private User user;
    private Product product;
    private Cart cart;
    private CartUser cartUser;
    private CartProduct cartProduct;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .nickname("Test User")
                .longitude(BigDecimal.valueOf(0.000000))
                .latitude(BigDecimal.valueOf(0.000000))
                .build();
        product = Product.builder()
                .productName("Test Product")
                .build();

        entityManager.persist(user);
        entityManager.persist(product);

    }

    @Test
    @Transactional
    @DisplayName("요청 받은 cartId의 cart 세부 정보를 확인할 수 있다.")
    void getDetail_withValidId() {
        //given
        cart = Cart.builder()
                .shoppingLocation("Test Location")
                .build();
        cartUser = CartUser.builder()
                .cart(cart)
                .user(user)
                .isOwner(true)
                .build();
        cartProduct = CartProduct.builder()
                .cart(cart)
                .product(product)
                .build();

        entityManager.persist(cart);
        entityManager.persist(cartUser);
        entityManager.persist(cartProduct);

        cart.addCartUser(cartUser);
        cart.addCartProduct(cartProduct);

        //when
        CartDTO.Detail result = cartService.getDetail(cart.getId());

        // then
        assertNotNull(result);
        assertThat(result.getNickname()).isEqualTo("Test User");
        assertThat(result.getShoppingLocation()).isEqualTo("Test Location");
        assertThat(result.getProducts()).hasSize(1);

    }

    @Test
    @Transactional
    @DisplayName("요청 받은 cartId의 cart가 없는 경우 Exception을 throw 한다.")
    public void getDetail_withInvalidCartId() {
        // when & then
        assertThatThrownBy(() -> cartService.getDetail(-1L)) // 존재하지 않는 id
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 카트를 찾을 수 없습니다");

    }

    @Test
    @Transactional
    @DisplayName("특정 유저와 가까운 Cart를 지도 zoom*5개 조회할 수 있다.")
    public void getNearCarts(){
        Integer zoom = 1;
        //given
        List<Cart> carts = List.of(
                createCart(BigDecimal.valueOf(38.200000), BigDecimal.valueOf(127.200000)),
                createCart(BigDecimal.valueOf(38.100000), BigDecimal.valueOf(127.100000)),
                createCart(BigDecimal.valueOf(38.400000), BigDecimal.valueOf(127.400000)),
                createCart(BigDecimal.valueOf(38.300000), BigDecimal.valueOf(127.300000)),
                createCart(BigDecimal.valueOf(38.600000), BigDecimal.valueOf(127.600000)),
                createCart(BigDecimal.valueOf(38.500000), BigDecimal.valueOf(127.500000)),
                createCart(BigDecimal.valueOf(38.800000), BigDecimal.valueOf(127.800000)),
                createCart(BigDecimal.valueOf(38.700000), BigDecimal.valueOf(127.700000))
        );
        cartRepository.saveAll(carts);

        //when
        PaginationListDTO<CartDTO.Near> nearCarts = cartService.getNearCarts(user.getId(), zoom);

        assertThat(nearCarts.getCount()).isEqualTo(5);
        assertThat(nearCarts.getData()).hasSize(5)
                .extracting("latitude")
                .containsExactlyInAnyOrder(
                        BigDecimal.valueOf(38.100000), BigDecimal.valueOf(38.200000),
                        BigDecimal.valueOf(38.300000), BigDecimal.valueOf(38.400000),
                        BigDecimal.valueOf(38.500000));

    }

    @Test
    @Transactional
    @DisplayName("요청받은 userId의 user가 존재하지 않는 경우  Exception을 throw 한다.")
    public void getNearCarts_withInvalidUserId(){
        assertThatThrownBy(() -> cartService.getNearCarts(-1L, 1)) // 존재하지 않는 id
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 사용자를 찾을 수 없습니다");
    }


    private Cart createCart(BigDecimal latitude, BigDecimal longitude){
        return Cart.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

}