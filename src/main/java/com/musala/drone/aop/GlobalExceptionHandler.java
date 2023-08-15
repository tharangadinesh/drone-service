package com.musala.drone.aop;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.musala.drone.exception.DroneException;
import com.musala.drone.exception.DroneNotFoundException;
import com.musala.drone.exception.LimitExceedException;
import com.musala.drone.exception.MedicationException;
import com.musala.drone.payload.response.ErrorResponse;
import com.musala.drone.util.MessageResource;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private MessageResource messageResource;

	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errorList = ex
				.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(fieldError -> fieldError.getField()+ ": "+ messageResource.getMessage(fieldError.getDefaultMessage() ))
				.collect(Collectors.toList());

		return new ResponseEntity<>(new ErrorResponse<>(errorList, request.getDescription(false)), status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String errorMessage = "Bad request. Malformed JSON or invalid request body.";
		return new ResponseEntity<>(new ErrorResponse<>(errorMessage, request.getDescription(false)), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(createErrorResponse(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ValidationException.class)
	public final ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request){
		return new ResponseEntity<>(createErrorResponse(ex, request), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DroneException.class)
	public final ResponseEntity<Object> handleDroneException(DroneException ex, WebRequest request) {
		return new ResponseEntity<>(createErrorResponse(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DroneNotFoundException.class)
	public final ResponseEntity<Object> handleDroneNotFoundException(DroneException ex, WebRequest request) {
		return new ResponseEntity<>(createErrorResponse(ex, request), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(LimitExceedException.class)
	public final ResponseEntity<Object> handleLimitExceedException(LimitExceedException ex, WebRequest request) {
		return new ResponseEntity<>(createErrorResponse(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(MedicationException.class)
	public final ResponseEntity<Object> handleMedicationException(MedicationException ex, WebRequest request){
		return new ResponseEntity<>(createErrorResponse(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private final ErrorResponse<String> createErrorResponse(Exception ex, WebRequest request) {
		return new ErrorResponse<>( ex.getMessage(), request.getDescription(false));
	}
}
