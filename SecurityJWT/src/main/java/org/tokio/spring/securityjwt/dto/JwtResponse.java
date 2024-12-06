package org.tokio.spring.securityjwt.dto;


import java.io.Serializable;

public record JwtResponse(String token) implements Serializable {

}
