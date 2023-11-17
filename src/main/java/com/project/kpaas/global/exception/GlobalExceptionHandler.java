package com.project.kpaas.global.exception;

import com.project.kpaas.global.dto.SuccessResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<SuccessResponseDto> methodValidException(MethodArgumentNotValidException e) {
        SuccessResponseDto responseDto = makeErrorResponse(e.getBindingResult());
        return ResponseEntity.badRequest().body(responseDto);
    }

    private SuccessResponseDto makeErrorResponse(BindingResult bindingResult) {
        String message = "";
        if (bindingResult.hasErrors()) {
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        return SuccessResponseDto.of(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleDataException throw Exception : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
