package org.tokio.spring.securityjwt.core.userDetails;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tokio.spring.securityjwt.dto.PermissionDTO;
import org.tokio.spring.securityjwt.dto.RoleDTO;
import org.tokio.spring.securityjwt.dto.UserDTO;
import org.tokio.spring.securityjwt.service.UserService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BasicUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // autorizacion
        try {
            Pair<UserDTO, String> tupleUserDto = userService.findUserAndPasswordByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("User with suername:%s not found".formatted(username)));

            // se adapta a user details de spring
            return toUserDetails(tupleUserDto.getLeft(), tupleUserDto.getRight());

        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Credenciales erróneas: {}".formatted(username), e);
        }
    }

    private UserDetails toUserDetails(UserDTO userDto,String password) {
        // Se crea la lista de autoridades a partir de los roles,
        // se puede considader un rol una autoridad, pero esto puede estar separado
        final List<SimpleGrantedAuthority> simpleGrantedAuthorities = userDto.getRoleDTOS().stream()
                .map(RoleDTO::getPermissionDTOS)
                .flatMap(Set::stream)
                .map(PermissionDTO::getName)
                .map(SimpleGrantedAuthority::new).toList();

        return new org.springframework.security.core.userdetails.User(
                userDto.getEmail(), // identidad
                //"{noop}%s".formatted(password), // credenciales (no encriptado)
                password, // credenciales (encriptado)
                simpleGrantedAuthorities// autoridades
        );
    }
}

