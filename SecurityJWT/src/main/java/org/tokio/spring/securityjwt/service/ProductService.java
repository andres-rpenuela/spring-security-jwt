package org.tokio.spring.securityjwt.service;

import org.tokio.spring.securityjwt.dto.ProductDTO;

import java.util.Set;

public interface ProductService {
    Set<ProductDTO> getAllProducts();

    Set<ProductDTO> getProductsByCategory(String category);
}
