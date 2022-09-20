package com.mariodicaprio.mamba.controllers;


import com.mariodicaprio.mamba.requests.RegisterRequest;
import com.mariodicaprio.mamba.responses.RegisterResponse;
import com.mariodicaprio.mamba.services.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/register")
@Transactional
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    ///////////////////////////////////////////////////////

    @PostMapping
    @Operation(description = "Registers a new user. Does not work if the user's username already exists.")
    @ApiResponse(responseCode = "200", description = "Whether thr registration was successful. If not, then the username already exists")
    public RegisterResponse register(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The registration request")
            RegisterRequest request) {
        return registerService.register(request);
    }

}
