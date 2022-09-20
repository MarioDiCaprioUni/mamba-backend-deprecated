package com.mariodicaprio.mamba.controllers;


import com.mariodicaprio.mamba.responses.UserBasicDataResponse;
import com.mariodicaprio.mamba.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /////////////////////////////////////////////////////////////////

    @GetMapping("/users")
    @Operation(description = "Searches for all users who match the given expression")
    public List<UserBasicDataResponse> users(
            @RequestParam
            @Parameter(description = "The expression to search for")
            String expression
    ) {
        return searchService.searchUsersByUsername(expression).stream().map(UserBasicDataResponse::new).toList();
    }

}
