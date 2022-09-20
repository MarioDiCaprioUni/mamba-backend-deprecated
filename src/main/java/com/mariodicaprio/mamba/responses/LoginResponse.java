package com.mariodicaprio.mamba.responses;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * This class represents the response body of the endpoint {@code /login}.
 * Each login attempt has the following properties:
 * <ul>
 *     <li>The username of the attempted login</li>
 *     <li>The password of the attempted login</li>
 *     <li>Whether the login attempt was valid</li>
 * </ul>
 */
@Data
public class LoginResponse {

    private final String username;

    private final String password;

    @NotNull
    private final boolean valid;

}
