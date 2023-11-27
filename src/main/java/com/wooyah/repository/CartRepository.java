package com.wooyah.repository;

import com.wooyah.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(value = "SELECT *, " +
            "(6371 * acos(cos(radians(?1)) * cos(radians(c.latitude)) " +
            "* cos(radians(c.longitude) - radians(?2)) + sin(radians(?1)) " +
            "* sin(radians(c.latitude)))) AS distance " +
            "FROM carts c ORDER BY distance ASC LIMIT ?3 ", nativeQuery = true)
    List<Cart> findCartsNearestTo(BigDecimal latitude, BigDecimal longitude, int limit);

}
