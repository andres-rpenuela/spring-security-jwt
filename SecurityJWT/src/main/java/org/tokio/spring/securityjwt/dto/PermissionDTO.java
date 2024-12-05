package org.tokio.spring.securityjwt.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for Permission")
public class PermissionDTO {

    private Long id;
    private String name;
    private String description;
}
