package org.tokio.spring.securityjwt.service.impl;

import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    @Transactional(readOnly = true) // aseguras que los datos relacionados con carga perezosa pueden cargarse durante la transacción.
    public Optional<Pair<UserDTO, String>> findUserAndPasswordByEmail(String mail) throws UserNotFoundException {
        final String maybeEmail = Optional.ofNullable(mail)
                .map(StringUtils::stripToNull)
                .orElseThrow(()->new UserNotFoundException("Email not allow"));

        // devuelve una tupa con el usuarioDTO y la pwd encriptada dentro de un optional
        // o un optional vacio
        return userDao.findUserByEmailEqualsIgnoreCase(maybeEmail)
                .map(user ->  Pair.of(modelMapper.map(user, UserDTO.class), user.getPassword()));
    }
}
