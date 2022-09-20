package com.mariodicaprio.mamba.controllers;


import com.mariodicaprio.mamba.requests.LoginRequest;
import com.mariodicaprio.mamba.responses.LoginResponse;
import com.mariodicaprio.mamba.services.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
@Transactional
public class LoginController {

    @Autowired
    private LoginService loginService;

    ////////////////////////////////////////////////////////////////

    @PostMapping
    @Operation(description = "Makes a login request and checks its validity")
    @ApiResponse(responseCode = "200", description = "The login response with the login's validity")
    public LoginResponse login(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The login request containing username and password")
            LoginRequest request
    ) {
        return loginService.login(request);
    }

}
