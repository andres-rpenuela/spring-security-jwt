package org.tokio.spring.securityjwt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tokio.spring.securityjwt.core.userDetails.BasicUserDetailsService;
import org.tokio.spring.securityjwt.core.util.JwtTokenUtil;
import org.tokio.spring.securityjwt.dto.JwtRequest;
import org.tokio.spring.securityjwt.dto.LoginDTO;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final BasicUserDetailsService basicUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping(value="/signin",consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> authenticateBasicUser(@RequestBody LoginDTO loginDto) throws Exception {
        log.info("Auth basic login");

        authenticateUser(loginDto.getUsername(),loginDto.getPassword());

        return new ResponseEntity<>("User signed-in successfully!. View and use the COOKIE of Response: JSESSIONID", HttpStatus.OK);
    }

    @PostMapping(value="/login",consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> authenticateJwtUser(@RequestBody JwtRequest jwtRequest) throws Exception {
        log.info("Jwt Token login");

        authenticateUser(jwtRequest.getUsername(),jwtRequest.getPassword());
        final UserDetails userDetails = basicUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        log.info("Jwt Token end %s".formatted(token) );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body("User signed-in successfully!. View and use the HEADER of Response: Authenticaciotn: "+token);
    }

    /**
     * Metodo para uthetnciar, tanto con Auth Basic, como con Jwt Token
     *
     * @param username
     * @param password
     * @throws Exception
     */
    private void authenticateUser(final String username,final String password) throws Exception {
        log.info("authentication user");
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        try{
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (DisabledException de){
            log.error(de.getMessage(), de);
            throw new Exception("User disabled",de);
        }catch (BadCredentialsException bce){
            log.error(bce.getMessage(), bce);
            throw new Exception("Bad credentials",bce);
        }catch (AuthenticationException ae){
            log.error(ae.getMessage(), ae);
            throw new Exception("Authentication exception",ae);
        }
    }
}
