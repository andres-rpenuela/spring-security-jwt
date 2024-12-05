package org.tokio.spring.securityjwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tokio.spring.securityjwt.core.constans.ErrorCode;
import org.tokio.spring.securityjwt.core.exception.ProductNotFoundException;
import org.tokio.spring.securityjwt.core.exception.UserNotFoundException;
import org.tokio.spring.securityjwt.core.response.ResponseError;
import org.tokio.spring.securityjwt.dto.UserDTO;
import org.tokio.spring.securityjwt.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "User", description = "Gestión de usuarios")
@Slf4j
public class UserRestController {

    private final UserService userService;


    @GetMapping(value = "/users",produces = "application/json",consumes = "application/json")
    @Operation(summary = "User given email")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "User with id given",content =
            @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
            ),
            @ApiResponse(responseCode = "400", description = "User not found",content =
            @Content(schema =  @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(responseCode = "500", description = "Error intern",content =
            @Content(schema =  @Schema(implementation = ResponseError.class))
            )})
    @Parameter(name = "email",description = "Email of user",schema = @Schema(implementation = Long.class))
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam(name = "email") String email){
        return ResponseEntity.ok( userService.findByEmail(email));
    }

    @GetMapping("/users/login-test")
    @Operation(summary = "User given email")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "User with id given",content =
            @Content(array = @ArraySchema(schema = @Schema(implementation = Pair.class)))
            ),
            @ApiResponse(responseCode = "400", description = "User not found",content =
            @Content(schema =  @Schema(implementation = ResponseError.class))
            ),
            @ApiResponse(responseCode = "500", description = "Error intern",content =
            @Content(schema =  @Schema(implementation = ResponseError.class))
            )})
    @Parameter(name = "email",description = "Email of user",schema = @Schema(implementation = Long.class))
    public ResponseEntity<Pair<UserDTO,String>> getUserLoginByEmail(@RequestParam(name = "email") String email){
        return ResponseEntity.ok( userService.findUserAndPasswordByEmail(email).orElseThrow(()-> new UserNotFoundException("User with email %s not found".formatted(email))));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseError> handleUserNotFoundException(ProductNotFoundException pnfe, HttpServletRequest request) {
        log.error(pnfe.getMessage(), pnfe);
        final ResponseError responseError = ResponseError.of( (long) ErrorCode.NOT_FOUND, pnfe.getMessage() );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }
}
