package org.tokio.spring.securityjwt.service;

import org.tokio.spring.securityjwt.dto.UserDTO;

public interface UserService {
    UserDTO findByEmail(String email);
}
