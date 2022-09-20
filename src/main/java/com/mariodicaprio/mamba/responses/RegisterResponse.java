package com.mariodicaprio.mamba.responses;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * This class represents the response body of the endpoint {@code /register}.
 * Each registration attempt has the following properties:
 * <ul>
 *     <li>The username of the attempted registration</li>
 *     <li>The email of the attempted registration</li>
 *     <li>The password of the attempted registration</li>
 *     <li>Whether the registration attempt was valid</li>
 * </ul>
 */
@Data
public class RegisterResponse {

    private final String username;

    private final String email;

    private final String password;

    @NotNull
    private final boolean valid;

}
