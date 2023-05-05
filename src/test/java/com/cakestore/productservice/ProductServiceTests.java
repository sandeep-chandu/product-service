package com.cakestore.productservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import com.cakestore.productservice.domain.Product;
import com.cakestore.productservice.dto.PatchProductDTO;
import com.cakestore.productservice.dto.ProductDTO;
import com.cakestore.productservice.repository.ProductRepo;
import com.cakestore.productservice.service.ProductServiceImpl;


@ExtendWith(MockitoExtension.class)
@Configuration
public class ProductServiceTests {

	@Mock
	private ProductRepo productRepo;
	
	@Spy
	ModelMapper modelMapper = new ModelMapper();
	
	@InjectMocks
	private ProductServiceImpl productService;
	
	
	@Test
	public void getAllProductsTest() {
		
		List<Product> listOfProducts = new ArrayList<>();
		listOfProducts.add(Product.builder().id((long) 1).title("Lemon cheesecake")
				.description("A cheesecake made of lemon")
				.imageUrl("https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.build());
		listOfProducts.add(Product.builder().id((long) 2).title("victoria sponge")
				.description("sponge with jam")
				.imageUrl("http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg")
				.build());
		listOfProducts.add(Product.builder().id((long) 3).title("Carrot Product")
				.description("Bugs bunnys favourite")
				.imageUrl("http://www.villageinn.com/i/pies/profile/carrotcake_main1.jpg")
				.build());
		
		when(productRepo.findAll()).thenReturn(listOfProducts);
		
		List<Product> productList = productService.getAll();
		
		assertEquals(productList.size(), listOfProducts.size());
		verify(productRepo, times(1)).findAll();
		
	}
	
	@Test
	public void getProductByIdTest() {
		
		long productId = 1L;
		Product product = Product.builder().description("A cheesecake made of lemon")
		.title("Lemon cheesecake")
		.imageUrl(
				"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
		.build();
		Optional<Product> productOptional = Optional.of(product);
		
		when(productRepo.findById(productId)).thenReturn(productOptional);
		
		Product productRes = productService.getProductById(productId);
		
		assertEquals(productRes.getDescription(), product.getDescription());
		assertEquals(product, productRes);
	}
	
	@Test
	public void patchUpdateProductByIdTest() {
		
		long productId = 1L;
		PatchProductDTO productDTO = PatchProductDTO.builder().id(productId).title("update Product title").build();
		Product product = Product.builder().description("A cheesecake made of lemon")
				.title("update Product title")
				.imageUrl(
						"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.build();
		this.modelMapper.createTypeMap(PatchProductDTO.class, Product.class);
		
		when(productRepo.findById(productId)).thenReturn(Optional.of(product));
		when(productRepo.save(any(Product.class))).thenAnswer((invocation) -> invocation.getArgument(0));
		
		Product productRes = productService.partialUpdateProduct(productDTO);
		
		assertEquals(productDTO.getTitle(), productRes.getTitle());
		assertNotEquals(productDTO.getDescription(), productRes.getDescription());
		assertNotEquals(productDTO.getImageUrl(), productRes.getImageUrl());
		assertEquals(product.getDescription(), productRes.getDescription());
		assertEquals(product.getImageUrl(), productRes.getImageUrl());
	}
	
	@Test
	public void deleteProductById() {
		
		long productId = 1L;
		
		doNothing().when(productRepo).deleteById(productId);
		
		productService.deleteProductById(productId);
		
		verify(productRepo, times(1)).deleteById(productId);
	}
	
	@Test
	public void saveCake() {
		ProductDTO createProductDTO = ProductDTO.builder().description("A cheesecake made of lemon")
				.title("Lemon cheesecake")
				.imageUrl(
						"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.build();
		Product product = Product.builder().id((long) 1L).description("A cheesecake made of lemon")
				.title("Lemon cheesecake")
				.imageUrl(
						"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.build();
		this.modelMapper.createTypeMap(ProductDTO.class, Product.class);
		
		
		when(productRepo.save(any(Product.class))).thenReturn(product);
		
		Product productRes = productService.save(createProductDTO);
		
		assertNotNull(productRes.getId());
		assertEquals(createProductDTO.getDescription(), productRes.getDescription());
		assertEquals(createProductDTO.getImageUrl(), productRes.getImageUrl());
		assertEquals(createProductDTO.getTitle(), productRes.getTitle());
		
	}
	
	@Test
	public void updateCake() {
		ProductDTO updateProductDTO = ProductDTO.builder()
				.id((long)1L)
				.description("A cheesecake with lemon flavour")
				.title("Lemon cheesecake")
				.imageUrl(
						"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.build();
		Product product = Product.builder().id((long) 1L).description("A cheesecake made of lemon")
				.title("Lemon cheesecake")
				.imageUrl(
						"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.build();
		this.modelMapper.createTypeMap(ProductDTO.class, Product.class);
		
		when(productRepo.findById(updateProductDTO.getId())).thenReturn(Optional.of(product));
		when(productRepo.save(any(Product.class))).thenAnswer((invocation) -> invocation.getArgument(0));
		
		Product productRes = productService.update(updateProductDTO);
		
		assertNotNull(productRes.getId());
		assertEquals(updateProductDTO.getDescription(), productRes.getDescription());
		assertEquals(product.getImageUrl(), productRes.getImageUrl());
		assertEquals(product.getTitle(), productRes.getTitle());
		
	}
}
