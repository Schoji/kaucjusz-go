package com.kaucjuszGo.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientPointsException.class)
    public ProblemDetail handleInsufficientPoints(InsufficientPointsException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Insufficient points");
        problem.setProperty("code", "INSUFFICIENT_POINTS");
        return problem;
    }

    @ExceptionHandler(DuplicatePointsAwardException.class)
    public ProblemDetail handleDuplicatePointsAward(DuplicatePointsAwardException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Duplicate point operation");
        problem.setProperty("code", "DUPLICATE_POINTS_OPERATION");
        return problem;
    }

    @ExceptionHandler(InvalidPointOperationException.class)
    public ProblemDetail handleInvalidPointOperation(InvalidPointOperationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Invalid point operation");
        problem.setProperty("code", "INVALID_POINT_OPERATION");
        return problem;
    }
}