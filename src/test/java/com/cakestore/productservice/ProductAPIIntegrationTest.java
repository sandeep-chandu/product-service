package com.cakestore.productservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cakestore.productservice.domain.Product;
import com.cakestore.productservice.dto.ProductDTO;
import com.cakestore.productservice.dto.PatchProductDTO;

@Configuration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestSecurityConfig.class)
@TestMethodOrder(OrderAnnotation.class)
public class ProductAPIIntegrationTest {

	  @LocalServerPort
	  private int port;

	  @Autowired
	  private TestRestTemplate restTemplate;
	  
	  private static HttpHeaders headers;

	  private final ObjectMapper objectMapper = new ObjectMapper();
	  
	  private String createURLWithPort() {
	        return "http://localhost:" + port + "/products";
	  }
	  
	  @BeforeAll
	  public static void init() {
	     headers = new HttpHeaders();
	     headers.setContentType(MediaType.APPLICATION_JSON);
	  }
	  
	  @Test
	  @Sql(statements = {"truncate table product"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	  @Sql(statements = {"truncate table product"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	  @Sql(scripts = "/data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	  @Order(1)
	  public void testCreateProduct() throws JsonProcessingException {
		  ProductDTO productDTO = ProductDTO.builder().title("Watermelon Product")
				  .description("A sponge cold Product with watermelon flavour")
				  .imageUrl("http://cakes.com/images/watermelon.jpg")
				  .price(new BigDecimal(10))
				  .build();
		  
		  HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(productDTO), headers);
		  ResponseEntity<Product> response = restTemplate.exchange(
	                createURLWithPort(), HttpMethod.POST, entity, Product.class);
		  
		  Product orderRes = response.getBody();
		  
		  assertEquals(HttpStatus.CREATED, response.getStatusCode());
	      assert orderRes != null;
	      assert orderRes.getId() != null;
	      assertEquals(productDTO.getTitle(), orderRes.getTitle());
	      assertEquals( productDTO.getDescription(), orderRes.getDescription());
	      assertEquals(productDTO.getImageUrl(), orderRes.getImageUrl());
	  }
	  
	  @Test
	  @Order(3)
	  public void testGetAllProducts() {
		  HttpEntity<String> entity = new HttpEntity<>(null, headers);
	        ResponseEntity<List<Product>> response = restTemplate.exchange(
	                createURLWithPort(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Product>>(){});
	        List<Product> cakeList = response.getBody();
	        
	        assert cakeList != null;
	        assertEquals(response.getStatusCode(), HttpStatus.OK);
	  }
	  
	  
	  @Test
	  @Order(2)
	  public void testGetProductById() throws JsonProcessingException {
		  HttpEntity<String> entity = new HttpEntity<>(null, headers);
		  ResponseEntity<Product> response = restTemplate.exchange(
	                createURLWithPort()+"/1", HttpMethod.GET, entity, new ParameterizedTypeReference<Product>(){});
		  
		  Product cakeRes = response.getBody();
		  
		  assert cakeRes != null;
		  assertEquals( HttpStatus.OK, response.getStatusCode());
		  String expected = "{\"id\":1,\"title\":\"almond cake\",\"description\":\"almond cake\",\"imageUrl\":\"https://abc.com/1\",\"price\":10.00}";
		  assertEquals(expected, objectMapper.writeValueAsString(cakeRes));
	  }
	 
	  
	  @Test
	  @Order(4)
	  public void testPartialUpdateProduct() throws JsonProcessingException {
		  long id = 1L;
		  RestTemplate restTemp = new RestTemplate();
		  restTemp.setRequestFactory(new HttpComponentsClientHttpRequestFactory()); 
		  PatchProductDTO patchProductDTO = PatchProductDTO.builder().description("A cheesecake with lemon flavour").build();
		  HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(patchProductDTO), headers);
		  ResponseEntity<Product> response = restTemp.exchange(
	                createURLWithPort()+"/"+id, HttpMethod.PATCH, entity, Product.class);
		  
		  Product orderRes = response.getBody();
		  assert orderRes != null;
	      assert orderRes.getId() != null;
	      assertEquals(orderRes.getDescription(), patchProductDTO.getDescription());
	      assertNotNull(orderRes.getImageUrl());
	      assertNotNull(orderRes.getTitle());
	  }
	  
	  @Test
	  @Order(5)
	  public void testUpdateProduct() throws JsonProcessingException {
		  long id = 4L;
		  ProductDTO productDTO = ProductDTO.builder().title("Watermelon Product")
				  .description("A sponge cold Product with watermelon flavour")
				  .imageUrl("http://cakes.com/images/watermelon.jpg")
				  .price(new BigDecimal(10))
				  .build();
		  HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(productDTO), headers);
		  ResponseEntity<Product> response = restTemplate.exchange(
	                createURLWithPort()+"/"+id, HttpMethod.PUT, entity, Product.class);
		  
		  Product orderRes = response.getBody();
		  assert orderRes != null;
	      assert orderRes.getId() != null;
	      assertEquals(orderRes.getDescription(), productDTO.getDescription());
	      assertEquals(orderRes.getTitle(), productDTO.getTitle());
	      assertEquals(orderRes.getImageUrl(), productDTO.getImageUrl());
	  }
	  
	  @Test
	  @Order(6)
	  public void testDeleteProduct() {
		  long id = 1L;
		  ResponseEntity<String> response = restTemplate.exchange(
	                createURLWithPort()+"/"+id, HttpMethod.DELETE, null, String.class);
		  
		  assertEquals(response.getStatusCode(), HttpStatus.OK);
	  }
	  
	  
}

