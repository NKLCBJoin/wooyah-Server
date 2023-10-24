package com.wooyah.service;

import com.wooyah.IntegrationTestSupport;
import com.wooyah.dto.cart.CartDTO;
import com.wooyah.entity.*;
import com.wooyah.entity.enums.CartStatus;
import com.wooyah.repository.CartRepository;
import com.wooyah.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class CartServiceTest extends IntegrationTestSupport {

    @Autowired
    private CartService cartService;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Product product;
    private Cart cart;
    private CartUser cartUser;
    private CartProduct cartProduct;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .nickname("Test User")
                .build();
        product = Product.builder()
                .productName("Test Product")
                .build();
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

        entityManager.persist(user);
        entityManager.persist(product);
        entityManager.persist(cart);
        entityManager.persist(cartUser);
        entityManager.persist(cartProduct);

        cart.addCartUser(cartUser);
        cart.addCartProduct(cartProduct);

    }

    @Test
    @Transactional
    @DisplayName("요청 받은 cartId의 cart 세부 정보를 확인할 수 있다.")
    void getDetail_withValidId() {
        //given

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
    public void getDetail_withInvalidId() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> cartService.getDetail(9999L)); // 존재하지 않는 id
    }

}