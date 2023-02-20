package com.wfms.exception;

import com.wfms.Dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex) {
        BaseResponse baseResponse = new BaseResponse(Integer.parseInt(ex.getMessage().split("@")[0]), ex.getMessage().split("@")[1]);
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);

    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> globalExceptionHandler(ResourceNotFoundException ex, MethodArgumentNotValidException exception) {
//        BaseResponse baseResponse= new BaseResponse(500,exception.getBindingResult()
//                .getFieldError().getDefaultMessage());
//        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//
//    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<?> resourceForbiddenRequestException(ResourceForbiddenRequestException ex ) {
//        BaseResponse baseResponse= new BaseResponse(Integer.parseInt(ex.getMessage().split("@")[0]),ex.getMessage().split("@")[1]);
//        return new ResponseEntity<>(baseResponse, HttpStatus.FORBIDDEN);
//
//    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public void handleConflict(HttpServletResponse response) throws IOException {
        response.sendError(403, "Bạn không có quyền truy cập trang này ");
    }

    @ExceptionHandler(ResourceBadRequestException.class)
    public ResponseEntity<?> resourceBadRequestException(ResourceBadRequestException ex) {
        BaseResponse baseResponse = new BaseResponse(Integer.parseInt(ex.getMessage().split("@")[0]), ex.getMessage().split("@")[1]);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> customValidationErrorHanding(MethodArgumentNotValidException exception) {
        BaseResponse baseResponse = new BaseResponse(400, exception.getBindingResult()
                .getFieldError().getDefaultMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }
}
