package org.tokio.spring.securityjwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Data transfer object for Product")
public class ProductDTO {

    @Schema(description = "Unique identifier of the product", example = "1", required = true)
    private Long id;

    @Schema(description = "Name of the product", example = "Laptop", required = true)
    private String name;
    private String description;
    private String category;

    @Schema(description = "Price of the product", example = "1500.00", required = true)
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal stock;

}
