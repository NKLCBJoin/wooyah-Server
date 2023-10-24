package com.wooyah.entity;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    private String nickname;

    private String profileImage;

    private String location;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String deviceNumber;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartUser> cartUsers = new ArrayList<>();
}
