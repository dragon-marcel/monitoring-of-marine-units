package backend.Marine.units.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class ErrorAPI {
//	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private HttpStatus status;
	
	public ErrorAPI(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
		this.timestamp = LocalDateTime.now();
	}
}
