package org.tokio.spring.securityjwt.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category;
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal stock;

    @Version
    private int version;
}
