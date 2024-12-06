package org.tokio.spring.securityjwt.core.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tokio.spring.securityjwt.core.util.JwtTokenUtil;

import java.io.IOException;

@Component
@DependsOn({"basicUserDetailsService","jwtTokenUtil"})
@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Qualifier("basicUserDetailsService")
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // comprueba si hay token en la petición http
        if( authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // si hay token, obtiene el usario
            jwtToken = authorizationHeader.substring(7);
            try{
                username = jwtTokenUtil.getUserName(jwtToken);
            }catch (IllegalArgumentException e) {
                log.error("No se puedo obtener el token.",e);
            }catch ( ExpiredJwtException e){
                log.error("El token ha expirado.",e);
            }
        }else{
            log.warn("El token no viene con Bearer delante.");
        }

        // comprueba si el usuairo del token esta authenticado
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // no lo esta, por lo que se authentica en el sistema
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtTokenUtil.validateToken(jwtToken,userDetails) ){
                // token no expirado, authentica el usuairo al sistmea
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                final WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
                usernamePasswordAuthenticationToken.setDetails(webAuthenticationDetails);

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
