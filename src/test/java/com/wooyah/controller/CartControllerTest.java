package com.wooyah.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooyah.dto.cart.CartDTO;
import com.wooyah.service.CartService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(CartController.class)
@WithMockUser
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Cart Detail을 조회할 수 있다.")
    public void getCartDetailTest() throws Exception {
        Long cartId = 1L;
        CartDTO.Detail cartDetail = mock(CartDTO.Detail.class);
        given(cartService.getDetail(cartId)).willReturn(cartDetail);


        mockMvc.perform(get("/api/cart/"+ cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("글 세부 정보"))
                .andExpect(jsonPath("$.result").isNotEmpty())
                .andDo(print());
    }
}