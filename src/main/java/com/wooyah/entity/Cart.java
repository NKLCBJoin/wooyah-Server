package com.wooyah.entity;

import com.wooyah.entity.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long id;

    private String shoppingLocation;

    private Integer participantNumber;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartUser> cartUsers = new ArrayList<>();

    public String getOwnerNickname() {
        return cartUsers.stream()
                .filter(CartUser::getIsOwner)
                .map(CartUser::getUser)
                .map(User::getNickname)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("카트 주인을 찾을 수 없습니다."));
    }

}
