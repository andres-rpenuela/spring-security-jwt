package org.tokio.spring.securityjwt.core.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.tokio.spring.securityjwt.core.converter.CollectionRoleToSetRoleDTOConverter;
import org.tokio.spring.securityjwt.domain.User;
import org.tokio.spring.securityjwt.dto.UserDTO;

@Configuration
public class UserDTOMapperConfiguration {

    public final ModelMapper modelMapper;

    public UserDTOMapperConfiguration(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        init();
    }

    private void init() {
        this.modelMapper.typeMap(User.class, UserDTO.class)
                .addMappings(mapping -> mapping.using(new CollectionRoleToSetRoleDTOConverter()).map(User::getRoles,UserDTO::setRoleDTOS));
    }
}
