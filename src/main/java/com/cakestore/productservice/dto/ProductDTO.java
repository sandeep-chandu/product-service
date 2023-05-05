package com.cakestore.productservice.dto;


import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
	
	@JsonIgnore
	@Schema(description = "Uniquq identifier of the product")
	private Long id;
	
	@NotEmpty
	@Schema(description = "Title of the product")
    private String title;

	@NotEmpty
	@Schema(description = "Description of the product")
    private String description;

	@NotEmpty
	@Pattern(regexp = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\-\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")
	@Schema(description = "Image URL of the product", defaultValue = "image url")
    private String imageUrl;
	
	@NotNull
	@Schema(description = "price of the product")
    private BigDecimal price;

}
