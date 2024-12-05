package org.tokio.spring.securityjwt.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tokio.spring.securityjwt.core.exception.UserNotFoundException;
import org.tokio.spring.securityjwt.dto.UserDTO;
import org.tokio.spring.securityjwt.report.UserDao;
import org.tokio.spring.securityjwt.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO findByEmail(String email) throws IllegalArgumentException, UserNotFoundException {
        final String maybeEmail = Optional.ofNullable(email)
                .orElseThrow(() -> new IllegalArgumentException("email is required"));

        return userDao.findUserByEmailEqualsIgnoreCase(maybeEmail)
                .map(u -> modelMapper.map(u, UserDTO.class))
                .orElseThrow(() -> new UserNotFoundException("User with email %s not found!".formatted(maybeEmail)));
    }
}
