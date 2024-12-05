package org.tokio.spring.securityjwt.core.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.tokio.spring.securityjwt.domain.Role;
import org.tokio.spring.securityjwt.dto.PermissionDTO;
import org.tokio.spring.securityjwt.dto.RoleDTO;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionRoleToSetRoleDTOConverter implements Converter<Collection<Role>, Set<RoleDTO>> {

    @Override
    public Set<RoleDTO> convert(MappingContext<Collection<Role>, Set<RoleDTO>> mappingContext) {
        if( mappingContext.getSource() == null ){
            return Set.of();
        }

        return mappingContext.getSource().stream().map(role -> {
            {
                final Set<PermissionDTO> permissionDTOS =
                        role.getPermissions().stream()
                                .map(permission ->
                                        PermissionDTO.builder()
                                                .id(permission.getId())
                                                .name(permission.getName().name())
                                                .description(permission.getDescription()).build())
                                .collect(Collectors.toSet());
                return  RoleDTO.builder()
                        .id(role.getId())
                        .name(role.getName().name())
                        .description(role.getDescription())
                        .permissionDTOS(permissionDTOS).build();

            }
        }).collect(Collectors.toSet());

    }
}
