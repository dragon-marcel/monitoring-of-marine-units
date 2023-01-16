package backend.Marine.units.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import backend.Marine.units.model.ErrorAPI;;

@ControllerAdvice
public class UserNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorAPI userNotFoundById(UserNotFoundException ex) {
		return new ErrorAPI(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ResponseBody
	@ExceptionHandler(UserUniqueException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorAPI userUniqueByUserName(UserUniqueException ex) {
		return new ErrorAPI(ex.getMessage(), HttpStatus.CONFLICT);
	}

	@ResponseBody
	@ExceptionHandler(InvalidAuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorAPI invalidAuth(InvalidAuthenticationException ex) {
		return new ErrorAPI(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ResponseBody
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorAPI usernameNotFound(UsernameNotFoundException ex) {
		return new ErrorAPI(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}
