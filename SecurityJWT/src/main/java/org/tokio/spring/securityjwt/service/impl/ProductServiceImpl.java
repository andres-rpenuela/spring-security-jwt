package org.tokio.spring.securityjwt.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tokio.spring.securityjwt.dto.ProductDTO;
import org.tokio.spring.securityjwt.report.ProductDao;
import org.tokio.spring.securityjwt.service.ProductService;

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
}
