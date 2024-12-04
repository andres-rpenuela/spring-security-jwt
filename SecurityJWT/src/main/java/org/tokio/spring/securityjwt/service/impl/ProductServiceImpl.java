package org.tokio.spring.securityjwt.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tokio.spring.securityjwt.core.exception.ProductNotFoundException;
import org.tokio.spring.securityjwt.domain.Product;
import org.tokio.spring.securityjwt.dto.ProductDTO;
import org.tokio.spring.securityjwt.report.ProductDao;
import org.tokio.spring.securityjwt.service.ProductService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;
    private final ProductDao productDao;

    @Override
    public Set<ProductDTO> getAllProducts() {
        return productDao.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ProductDTO> getProductsByCategory(String category) {
        final Optional<String> maybeCategory = Optional.ofNullable(category)
                .map(StringUtils::stripToNull);

        return maybeCategory.map(s -> productDao.findByCategory(s).stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toSet()))
                .orElseGet(this::getAllProducts);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Long idProduct = Optional.ofNullable(id)
                .orElseThrow(() -> new ProductNotFoundException(id));


        return  productDao.findById(idProduct)
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = new Product();
        populationCreateAndEditProduct(product, productDTO);

        return this.modelMapperProductToProductDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(long id, ProductDTO productDTO)  throws ProductNotFoundException {
        Product product = productDao.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
        populationCreateAndEditProduct(product, productDTO);

        return this.modelMapperProductToProductDTO(product);
    }

    private void populationCreateAndEditProduct(Product product, ProductDTO productDTO) {
        final LocalDateTime now = LocalDateTime.now();
        // set or update data
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setStock(productDTO.getStock());
        product.setDescription(productDTO.getDescription());
        if(product.getCreatedAt() == null){
            product.setCreatedAt(now);
        }
        product.setUpdatedAt(now);

        productDao.save(product);
    }

    private ProductDTO modelMapperProductToProductDTO(@NonNull Product product) {
        return modelMapper.map(product,ProductDTO.class);
    }
}
