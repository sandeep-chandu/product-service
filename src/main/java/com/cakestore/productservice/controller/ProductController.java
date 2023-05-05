package com.cakestore.productservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cakestore.productservice.domain.Product;
import com.cakestore.productservice.dto.PatchProductDTO;
import com.cakestore.productservice.dto.ProductDTO;
import com.cakestore.productservice.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping
	@Operation(summary = "Get all product", description = "Get list of all the products avaialable")
	@SecurityRequirement(name = "Bearer Authentication")
	ResponseEntity<List<Product>> getAllProducts(){
		return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get product details", description = "Get details of a specific product by id")
	@SecurityRequirement(name = "Bearer Authentication")
	ResponseEntity<Product> getProductById(@PathVariable("id") Long id){
		return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
	}
	
	@PostMapping
	@Operation(summary = "Create Product", description = "Create or register a new product")
	@SecurityRequirement(name = "Bearer Authentication")
	ResponseEntity<Product> createProduct(@RequestBody @Valid ProductDTO createProduct){
		return new ResponseEntity<>(productService.save(createProduct), HttpStatus.CREATED);
	}
	
	@PatchMapping("/{id}")
	@Operation(summary = "Update Product details", description = "Update details of a specific product of by just passing details you want to update")
	@SecurityRequirement(name = "Bearer Authentication")
	ResponseEntity<Product> partialUpdateProduct(@PathVariable Long id, @RequestBody @Valid PatchProductDTO updateProductDto){
		updateProductDto.setId(id);
		return new ResponseEntity<>(productService.partialUpdateProduct(updateProductDto), HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Update product", description = "Create or register a new product")
	@SecurityRequirement(name = "Bearer Authentication")
	ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO updateProduct){
		updateProduct.setId(id);
		return new ResponseEntity<>(productService.update(updateProduct), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete product", description = "Delete or remove an existing product")
	@SecurityRequirement(name = "Bearer Authentication")
	 public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		productService.deleteProductById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
