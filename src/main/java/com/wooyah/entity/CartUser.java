package com.wooyah.entity;

import com.wooyah.entity.enums.CartUserStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="cart_users")
public class CartUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private Boolean isOwner;

    @Enumerated(EnumType.STRING)
    private CartUserStatus status;


    //이게 필요할까?
    public void setUser(User user){
        this.user = user;
        user.getCartUsers().add(this);
    }

    public void setCart(Cart cart){
        this.cart = cart;
        cart.getCartUsers().add(this);
    }

}