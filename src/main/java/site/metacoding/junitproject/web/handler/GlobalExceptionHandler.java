package site.metacoding.junitproject.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import site.metacoding.junitproject.web.dto.response.CMRespDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    //모든 파싱??할때의 익셉션을 잡아챈다..!
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> apiException(RuntimeException e){

        return new ResponseEntity<>(CMRespDto.builder().code(-1).msg(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }
}
