package org.tokio.spring.securityjwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tokio.spring.securityjwt.core.response.ResponseError;
import org.tokio.spring.securityjwt.dto.ProductDTO;
import org.tokio.spring.securityjwt.service.ProductService;

import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/products")
    @Operation(summary = "Listado de productos")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Listado de productos",content =
                @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))
            ),
            @ApiResponse(responseCode = "500", description = "Error interno",content =
                @Content(schema =  @Schema(implementation = ResponseError.class))
            )})
    @Parameter(name = "category",description = "Filter by category of list. This param is optional.")
    public ResponseEntity<Set<ProductDTO>> getAllProductsHandler(@RequestParam(name = "category", defaultValue = "") String category) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductsByCategory(category));
    }

}
