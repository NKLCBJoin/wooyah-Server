package com.wooyah.repository;

import com.wooyah.entity.Cart;
import com.wooyah.entity.CartUser;
import com.wooyah.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartUserRepository extends JpaRepository<CartUser, Long> {
    Optional<CartUser> findByUserAndCart(User user, Cart cart);

    List<CartUser> findALlByUser(User user);
}
