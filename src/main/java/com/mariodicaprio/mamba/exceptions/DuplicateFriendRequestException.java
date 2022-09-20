package com.mariodicaprio.mamba.exceptions;


import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@StandardException
@ResponseStatus(value = HttpStatus.LOOP_DETECTED, reason = "Friend request already pending")
public class DuplicateFriendRequestException extends Exception {

}
