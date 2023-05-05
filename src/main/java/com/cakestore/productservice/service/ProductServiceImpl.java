package com.cakestore.productservice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cakestore.productservice.domain.Product;
import com.cakestore.productservice.dto.PatchProductDTO;
import com.cakestore.productservice.dto.ProductDTO;
import com.cakestore.productservice.repository.ProductRepo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public Product save(ProductDTO createProductDto) {
		TypeMap<ProductDTO, Product> propertyMapper = modelMapper.getTypeMap(ProductDTO.class, Product.class);
		Product product= new Product();
		propertyMapper.map(createProductDto, product);
		return productRepo.save(product);
	}

	@Override
	public List<Product> getAll() {
		return productRepo.findAll();
	}

	@Override
	public Product getProductById(Long id) {
		Optional<Product> productOptional = productRepo.findById(id);
		productOptional.orElseThrow(EntityNotFoundException::new);
		return productOptional.get();
	}

	@Override
	public Product partialUpdateProduct(PatchProductDTO productDto) {
		Optional<Product> productOptional = productRepo.findById(productDto.getId());
		productOptional.orElseThrow(EntityNotFoundException::new);
		
			Product product = productOptional.get();
			TypeMap<PatchProductDTO, Product> propertyMapper = modelMapper.getTypeMap(PatchProductDTO.class, Product.class);
		    propertyMapper.addMappings(mapper -> mapper.when(Conditions.isNull()).skip(PatchProductDTO::getImageUrl, Product::setImageUrl));
		    propertyMapper.addMappings(mapper -> mapper.when(Conditions.isNull()).skip(PatchProductDTO::getDescription, Product::setDescription));
		    propertyMapper.addMappings(mapper -> mapper.when(Conditions.isNull()).skip(PatchProductDTO::getTitle, Product::setTitle));
		    propertyMapper.addMappings(mapper -> mapper.when(Conditions.isNull()).skip(PatchProductDTO::getPrice, Product::setPrice));
		    propertyMapper.map(productDto, product);
		    return productRepo.save(product);
	}

	@Override
	public void deleteProductById(Long id) {
		productRepo.deleteById(id);
	}

	@Override
	public Product update(ProductDTO productDto) {
		Optional<Product> productOptional = productRepo.findById(productDto.getId());
		productOptional.orElseThrow(EntityNotFoundException::new);
		
			TypeMap<ProductDTO, Product> propertyMapper = modelMapper.getTypeMap(ProductDTO.class, Product.class);
			Product cake= new Product();
			propertyMapper.map(productDto, cake);
			return productRepo.save(cake);
	}

}
