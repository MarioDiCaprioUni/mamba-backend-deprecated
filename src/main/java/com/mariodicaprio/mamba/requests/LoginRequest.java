package com.mariodicaprio.mamba.requests;


import lombok.Data;


/**
 * This class represents the request body of the endpoint {@code /login}.
 * Each login attempt has the following properties:
 * <ul>
 *     <li>The username of the attempted login</li>
 *     <li>The password of the attempted login</li>
 * </ul>
 */
@Data
public class LoginRequest {

    private final String username;
    private final String password;

}
