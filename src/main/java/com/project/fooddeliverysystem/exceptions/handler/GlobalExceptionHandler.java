package com.project.fooddeliverysystem.exceptions.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	ResponseDto responseDto;
	
	@ExceptionHandler(value = AlreadyExistException.class)
	public ResponseEntity<ResponseDto> alreadyExistException(AlreadyExistException ex) {
		responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(),new Date(), null );
		return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<ResponseDto> notFoundException(NotFoundException ex) {
		responseDto = new ResponseDto(HttpStatus.NOT_FOUND.toString(), ex.getMessage(),new Date(), null );
		return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.NOT_FOUND);
	}
}
