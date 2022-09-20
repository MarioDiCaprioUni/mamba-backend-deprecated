package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SearchService {

    @Autowired
    private UserRepository userRepository;

    ////////////////////////////////////////////////////////////////////

    public List<User> searchUsersByUsername(String expression) {
        return userRepository.findByUsernameContaining(expression);
    }

}
