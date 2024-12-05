package org.tokio.spring.securityjwt.service;


import org.apache.commons.lang3.tuple.Pair;
import org.tokio.spring.securityjwt.core.exception.UserNotFoundException;
import org.tokio.spring.securityjwt.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    UserDTO findByEmail(String email);

    Optional<Pair<UserDTO, String>> findUserAndPasswordByEmail(String mail) throws UserNotFoundException;
}
