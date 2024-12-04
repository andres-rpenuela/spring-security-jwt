package org.tokio.spring.securityjwt.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.securityjwt.domain.Product;

import java.util.Set;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    Set<Product> findByCategory(String category);
}
