package org.tokio.spring.securityjwt.core.userDetails;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tokio.spring.securityjwt.dto.RoleDTO;
import org.tokio.spring.securityjwt.dto.UserDTO;
import org.tokio.spring.securityjwt.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        /*
        final List<SimpleGrantedAuthority> simpleGrantedAuthorities = userDto.getRoleDTOS().stream()
                .map(RoleDTO::getPermissionDTOS)
                .flatMap(Set::stream)
                .map(PermissionDTO::getName)
                .map(SimpleGrantedAuthority::new).toList();*/
        //
        final List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) getAuthorities( userDto.getRoleDTOS() );
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        userDto.getEmail(), // identidad
                        //"{noop}%s".formatted(password), // credenciales (no encriptado)
                        password, // credenciales (encriptado)
                        grantedAuthorities// autoridades
                );

        return userDetails;
    }

    /**
     * Role + Authorities
     * https://www.baeldung.com/spring-security-granted-authority-vs-role
     * @param roles
     * @return
     */
    private List<? extends GrantedAuthority> getAuthorities(
            Collection<RoleDTO> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleDTO role: roles) {
            //add ROLE:  hasRole(“ADMIN”),
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            // add authority: hasAuthority(‘READ_AUTHORITY’), we are restricting access in a fine-grained manner.
            authorities.addAll(role.getPermissionDTOS()
                    .stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .toList());
        }

        return authorities;
    }
}

