package com.mariodicaprio.mamba.exceptions;


import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@StandardException
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User does not exist")
public class UserDoesNotExistException extends Exception {

}
