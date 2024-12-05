package org.tokio.spring.securityjwt.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDTO {

    private Long id;
    private String name;
    private String description;
    private Set<PermissionDTO> permissionDTOS;
}
