package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class SearchService {

    @Autowired
    private UserRepository userRepository;

    ////////////////////////////////////////////////////////////////////

    @Transactional(readOnly = true)
    public List<User> searchUsersByUsername(String expression) {
        return userRepository.findFirst10ByUsernameContaining(expression);
    }

}
