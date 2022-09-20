package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.UserRepository;
import com.mariodicaprio.mamba.requests.LoginRequest;
import com.mariodicaprio.mamba.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    ////////////////////////////////////////////////////////////////////////////

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        boolean valid = isLoginValid(request);
        return new LoginResponse(username, password, valid);
    }

    public boolean isLoginValid(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        return  user != null &&
                user.getUsername() != null &&
                user.getPassword() != null &&
                request.getUsername() != null &&
                request.getPassword() != null &&
                request.getUsername().equals(user.getUsername()) &&
                passwordEncoder.matches(request.getPassword(), user.getPassword());
    }

}
