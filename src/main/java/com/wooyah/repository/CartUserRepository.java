package com.wooyah.repository;

import com.wooyah.entity.CartUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartUserRepository extends JpaRepository<CartUser, Long> {
}
