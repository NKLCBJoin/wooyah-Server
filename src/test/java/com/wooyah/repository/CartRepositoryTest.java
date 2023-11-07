package com.wooyah.repository;

import com.wooyah.entity.Cart;
import com.wooyah.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class CartRepositoryTest{

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .nickname("Test User")
                .longitude(BigDecimal.valueOf(38.000000))
                .latitude(BigDecimal.valueOf(127.000000))
                .build();

        entityManager.persist(user);
    }

    @AfterEach
    void tearDown(){
        cartRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("특정 유저 위치에서 가까운 Cart들을 조회할 수 있다.")
    void findCartsNearestTo() {
        final int LIMIT = 2;
        // given
        List<Cart> carts = List.of(
                createCart(BigDecimal.valueOf(38.200000), BigDecimal.valueOf(127.200000)),
                createCart(BigDecimal.valueOf(38.100000), BigDecimal.valueOf(127.100000)),
                createCart(BigDecimal.valueOf(38.300000), BigDecimal.valueOf(127.300000)),
                createCart(BigDecimal.valueOf(38.500000), BigDecimal.valueOf(127.500000)),
                createCart(BigDecimal.valueOf(38.400000), BigDecimal.valueOf(127.400000)));
        cartRepository.saveAll(carts);

        //when
        List<Cart> nearestCarts = cartRepository.findCartsNearestTo(user.getLongitude(), user.getLatitude(), LIMIT);

        //then
        assertThat(nearestCarts).hasSize(2)
                .extracting("longitude")
                .containsExactlyInAnyOrder(
                        BigDecimal.valueOf(38.100000),
                        BigDecimal.valueOf(38.200000));

    }

    private Cart createCart(BigDecimal longitude, BigDecimal latitude){
        return Cart.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}