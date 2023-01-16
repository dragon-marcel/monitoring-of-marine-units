package backend.Marine.units.contoler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.Marine.units.service.WeatherApiService;
import dto.WeatherDTO;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/weather")
public class WeatherController {
	private final WeatherApiService weatherApiService;

	public WeatherController(WeatherApiService weatherApiService) {
		this.weatherApiService = weatherApiService;
	}

	@GetMapping("/lat/{lat}/lon/{lon}")
	@ApiOperation(value = "Get weather by lat lon")
	public ResponseEntity<WeatherDTO> getWeatherbyLatLon(@PathVariable double lat, @PathVariable double lon) {
		WeatherDTO weather = weatherApiService.getWeatherForPosition(lat, lon);

		return new ResponseEntity<WeatherDTO>(weather, HttpStatus.OK);
	}

}
