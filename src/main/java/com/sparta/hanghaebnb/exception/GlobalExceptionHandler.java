package com.sparta.hanghaebnb.exception;

import com.sparta.hanghaebnb.dto.response.MessageResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class )
    public MessageResponseDto methodValidException(MethodArgumentNotValidException e) {
        MessageResponseDto responseDto = makeErrorResponse(e.getBindingResult());
        log.error("methodValidException throw Exception : {}", e.getBindingResult());
        return MessageResponseDto.of(responseDto.getMsg(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleDataException throw Exception : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ErrorResponse> bindRequsetDtoException(BindException e) {
        log.error("handleDataException throw Exception : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_ARGUMENT);
    }

    private MessageResponseDto makeErrorResponse(BindingResult bindingResult) {
        String message = "";
        if (bindingResult.hasErrors()) {
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        return MessageResponseDto.of(message, HttpStatus.BAD_REQUEST);
    }
}
