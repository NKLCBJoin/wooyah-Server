package com.wooyah.service;

import com.wooyah.entity.Product;
import com.wooyah.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;

    @Transactional
    public List<Product> saveProducts(List<String> productNames) {

        List<Product> products = new ArrayList<>();

        for (String productName : productNames) {

            //이름 검색후 없으면 새로 생성
            Product product = productRepository.findByProductName(productName)
                    .orElseGet(() -> Product.builder()
                            .productName(productName)
                            .build());

            products.add(productRepository.save(product));
        }
        return products;
    }
}