package org.tokio.spring.securityjwt.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.tokio.spring.securityjwt.core.constans.PRIVILEGES;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PERMISSIONS")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PRIVILEGES name;
    private String description;

    @CreationTimestamp
    @JoinColumn(name="created_at")
    private LocalDateTime createdAt;

}
