package com.wooyah.dto.user;

import com.wooyah.entity.Cart;
import com.wooyah.entity.User;
import lombok.Builder;
import lombok.Data;

public class UserDTO {

    @Data
    @Builder
    public static class Phone{
        private String phone;

        public static UserDTO.Phone from(User user){
            return UserDTO.Phone.builder()
                    .phone(user.getPhone())
                    .build();
        }
    }

    @Data
    @Builder
    public static class Detail{
        private Long cartId;
        private String shoppingLocation;
        private Integer participantNumber;

        public static UserDTO.Detail of(Cart cart, Integer participantNumber){
            return Detail.builder()
                    .cartId(cart.getId())
                    .shoppingLocation(cart.getShoppingLocation())
                    .participantNumber(participantNumber)
                    .build();
        }
    }



}
