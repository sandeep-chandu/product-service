package com.cakestore.productservice.service;

import java.util.List;

import com.cakestore.productservice.domain.Product;
import com.cakestore.productservice.dto.PatchProductDTO;
import com.cakestore.productservice.dto.ProductDTO;

public interface ProductService {
	
	Product save(ProductDTO createProductDto);
	
	Product update(ProductDTO cake);
	
	List<Product> getAll();
	
	Product getProductById(Long id);
	
	Product partialUpdateProduct(PatchProductDTO productDto);
	
	void deleteProductById(Long id);

}
