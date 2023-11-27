package com.wooyah.entity;

import com.wooyah.entity.base.BaseEntity;
import com.wooyah.entity.enums.Gender;
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
@Table(name="users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    private String nickname;

    private String profileImage;

    private String location;

    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String deviceNumber;

    private BigDecimal latitude;

    private BigDecimal longitude;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartUser> cartUsers = new ArrayList<>();
}