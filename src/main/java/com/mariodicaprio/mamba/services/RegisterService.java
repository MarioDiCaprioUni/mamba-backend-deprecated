package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.UserRepository;
import com.mariodicaprio.mamba.responses.RegisterResponse;
import com.mariodicaprio.mamba.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /////////////////////////////////////////////////////////////////////////

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RegisterResponse register(RegisterRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();
        boolean valid = !userRepository.existsByUsername(username);
        if (valid) {
            User user = new User(username, email, passwordEncoder.encode(password));
            userRepository.save(user);
        }
        return new RegisterResponse(username, email, password, valid);
    }

}
