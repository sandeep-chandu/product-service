package com.cakestore.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakestore.productservice.domain.Product;


public interface ProductRepo extends JpaRepository<Product, Long> {

}
