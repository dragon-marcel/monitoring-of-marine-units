
package backend.Marine.units.service;

import backend.Marine.units.dto.WeatherDTO;

public interface WeatherDataFetcherService {
	WeatherDTO getWeatherForPosition(double lat, double lon);

}
