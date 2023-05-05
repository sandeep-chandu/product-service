package com.cakestore.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cakestore.productservice.domain.Product;
import com.cakestore.productservice.dto.PatchProductDTO;
import com.cakestore.productservice.dto.ProductDTO;
import com.cakestore.productservice.repository.ProductRepo;
import com.cakestore.productservice.service.ProductService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@MockBean
	private ProductRepo productRepo;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void givenProductObject_whenCreateProduct_thenReturnSavedProduct() throws Exception {

		Product product = Product.builder().id((long) 1)
				.description("almond cake")
				.title("almond cake")
				.imageUrl("https://abc.com/1")
				.price(new BigDecimal(10))
				.build();

		given(productService.save(any(ProductDTO.class))).willReturn(product);

		ResultActions response = mockMvc.perform(post("/products")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(product)));

		response.andDo(print()).
	                andExpect(status().isCreated())
	                .andExpect(jsonPath("$.title",
	                        is(product.getTitle())))
	                .andExpect(jsonPath("$.description",
	                        is(product.getDescription())))
	                .andExpect(jsonPath("$.imageUrl",
	                        is(product.getImageUrl())));

	}
	
	@Test
	public void givenListOfProducts_WhenGetAllProducts_thenReturnProductList() throws Exception {
		
		List<Product> listOfProducts = new ArrayList<>();
		listOfProducts.add(Product.builder().id((long) 1).title("Lemon cheesecake")
				.description("A cheesecake made of lemon")
				.imageUrl("https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.price(new BigDecimal(10))
				.build());
		listOfProducts.add(Product.builder().id((long) 2).title("victoria sponge")
				.description("sponge with jam")
				.imageUrl("http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg")
				.price(new BigDecimal(10))
				.build());
		listOfProducts.add(Product.builder().id((long) 3).title("Carrot product")
				.description("Bugs bunnys favourite")
				.imageUrl("http://www.villageinn.com/i/pies/profile/carrotcake_main1.jpg")
				.price(new BigDecimal(10))
				.build());
		
		given(productService.getAll()).willReturn(listOfProducts);
		
		ResultActions response = mockMvc.perform(get("/products"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(listOfProducts.size())));
		
	}
	
	@Test
	public void givenProductId_whenGetProductById_thenReturnProductObject() throws Exception{
		
		long productId = 1L;
		Product product = Product.builder().description("A cheesecake made of lemon")
				.title("Lemon cheesecake")
				.imageUrl(
						"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.price(new BigDecimal(10))
				.build();
		given(productService.getProductById(productId)).willReturn(product);
		
		ResultActions response = mockMvc.perform(get("/products/{id}", productId));

		response.andExpect(status().isOk()).andDo(print())
			.andExpect(jsonPath("$.title", is(product.getTitle())))
			.andExpect(jsonPath("$.description", is(product.getDescription())))
			.andExpect(jsonPath("$.imageUrl", is(product.getImageUrl())));
	}
	
	@Test
	public void giveCakeDTOAndProductId_whenPatchProductById_thenReturnProductObject() throws Exception{
		
		long productId = 1L;
		PatchProductDTO cakeDto = PatchProductDTO.builder().title("update product title").build();
		Product product = Product.builder().description("A cheesecake made of lemon")
				.title("update product title")
				.imageUrl(
						"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.price(new BigDecimal(10))
				.build();
		given(productRepo.findById(productId)).willReturn(Optional.empty());
		given(productService.partialUpdateProduct(any(PatchProductDTO.class))).willReturn(product);
		
		ResultActions response = mockMvc.perform(patch("/products/{id}", productId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cakeDto)));
		
		response.andExpect(status().isOk()).andDo(print())
		.andExpect(jsonPath("$.title", is(product.getTitle())))
		.andExpect(jsonPath("$.description", is(product.getDescription())))
		.andExpect(jsonPath("$.imageUrl", is(product.getImageUrl())));
		
	}
	
	@Test
	public void givenProductObject_whenUpdateProduct_thenReturnSavedCake() throws Exception {

		ProductDTO updateProductDto = ProductDTO.builder().description("A cheesecake made of lemon")
				.title("Lemon cheesecake")
				.imageUrl(
						"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.price(new BigDecimal(10))
				.build();
		
		Product product = Product.builder().id((long) 1L)
				.description("A cheesecake made of lemon")
				.title("Lemon cheesecake")
				.imageUrl(
						"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg")
				.price(new BigDecimal(10))
				.build();

		given(productService.update(any(ProductDTO.class))).willReturn(product);

		ResultActions response = mockMvc.perform(put("/products/{id}", (long)1L)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(updateProductDto)));

		response.andDo(print()).
	                andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
	                .andExpect(jsonPath("$.title",
	                        is(product.getTitle())))
	                .andExpect(jsonPath("$.description",
	                        is(product.getDescription())))
	                .andExpect(jsonPath("$.imageUrl",
	                        is(product.getImageUrl())));

	}
	
	
	@Test
	public void giveProductId_whenDeleteProductById_thenReturnNothing() throws Exception{
		
		long productId = 1L;
		
		willDoNothing().given(productService).deleteProductById(null);
		
		ResultActions response = mockMvc.perform(delete("/products/{id}", productId));
		 response.andExpect(status().isOk());
	}
	
}
