package org.tokio.spring.securityjwt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tokio.spring.securityjwt.dto.LoginDTO;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;

    @PostMapping(value="/signin",consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDTO loginDto) throws Exception {
        log.info("Auth basic login");

        authenticateUser(loginDto.getUsername(),loginDto.getPassword());

        return new ResponseEntity<>("User signed-in successfully!. View and use the COOKIE of Response: JSESSIONID", HttpStatus.OK);
    }

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
