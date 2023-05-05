package com.cakestore.productservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cakestore.productservice.domain.Product;
import com.cakestore.productservice.dto.ProductDTO;
import com.cakestore.productservice.dto.PatchProductDTO;

@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.createTypeMap(ProductDTO.class, Product.class);
		modelMapper.createTypeMap(PatchProductDTO.class, Product.class);
	    return modelMapper;
	}
}
