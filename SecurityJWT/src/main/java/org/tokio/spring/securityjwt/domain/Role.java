package org.tokio.spring.securityjwt.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.tokio.spring.securityjwt.core.constans.ROLES;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ROLES")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ROLES name;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Collection<Permission> permissions;

    @CreationTimestamp
    @JoinColumn(name="created_at")
    private LocalDateTime createdAt;

}
