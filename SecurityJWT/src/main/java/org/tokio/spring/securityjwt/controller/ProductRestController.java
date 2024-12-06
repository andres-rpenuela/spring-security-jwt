package org.tokio.spring.securityjwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tokio.spring.securityjwt.core.constans.ErrorCode;
import org.tokio.spring.securityjwt.core.exception.ProductNotFoundException;
import org.tokio.spring.securityjwt.core.response.ResponseError;
import org.tokio.spring.securityjwt.dto.ProductDTO;
import org.tokio.spring.securityjwt.service.ProductService;

import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos")
@Slf4j
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
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<Set<ProductDTO>> getAllProductsHandler(@RequestParam(name = "category", defaultValue = "") String category) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductsByCategory(category));
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Product given id")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Prodcut with id given",content =
            @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))
            ),
            @ApiResponse(responseCode = "400", description = "Product not found",content =
            @Content(schema =  @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(responseCode = "500", description = "Error interno",content =
            @Content(schema =  @Schema(implementation = ResponseError.class))
            )})
    @Parameter(name = "id",description = "Identification of product",schema = @Schema(implementation = Long.class))
    public ResponseEntity<ProductDTO> getProductByIdHandler(@PathVariable(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(id));
    }

    @PostMapping(value="/products",produces = "application/json",consumes = "application/json")
    @Operation(
            summary = "Create a new product",
            description = "This endpoint creates a new product using the provided ProductDTO.",
            tags = { "Product" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(responseCode = "500", description = "Error interno",content = @Content(
                    mediaType = "application/json",
                    schema =  @Schema(implementation = ResponseError.class))
            )
    })
    public ResponseEntity<ProductDTO> createProductHandler(@RequestBody ProductDTO productDTO) {
        ProductDTO addProductDTO = productService.addProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addProductDTO);
    }

    @PutMapping(value = "/products/{id}", produces = "application/json",consumes = "application/json")
    @Operation(
            summary = "Create a new product",
            description = "This endpoint creates a new product using the provided ProductDTO.",
            tags = { "Product" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(responseCode = "500", description = "Error interno",content = @Content(
                    mediaType = "application/json",
                    schema =  @Schema(implementation = ResponseError.class))
            )
    })
    @Parameter(
            description = "ID of the product to update",
            required = true,
            example = "123"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated product details",
            required = true,
            content = @Content(schema = @Schema(implementation = ProductDTO.class))
    )
    public ResponseEntity<ProductDTO> updateProductHandler(@PathVariable(name="id") String id, @RequestBody ProductDTO productDTO) {
        ProductDTO updateProduct =  productService.updateProduct(Long.parseLong(id),productDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ResponseError> handleProductNotFoundException(ProductNotFoundException pnfe, HttpServletRequest request) {
        log.error(pnfe.getMessage(), pnfe);
        final ResponseError responseError = ResponseError.of( (long) ErrorCode.NOT_FOUND, pnfe.getMessage() );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }
}
