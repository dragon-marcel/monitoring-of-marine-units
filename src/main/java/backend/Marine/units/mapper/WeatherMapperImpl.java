package backend.Marine.units.mapper;

import backend.Marine.units.dto.WeatherDTO;
import backend.Marine.units.model.weather.WeatherConditions;

public class WeatherMapperImpl implements WeatherMapper {

	@Override
	public WeatherDTO toWeatherDTO(WeatherConditions weather) {
		if (weather == null)
			throw new IllegalArgumentException("Weather conditions cannot be null");

		WeatherDTO weatherDTO = new WeatherDTO();
		weatherDTO.setTemp(weather.getMain().getTemp());
		weatherDTO.setDescription(weather.getWeather().get(0).getDescription());
		weatherDTO.setIcon(weather.getWeather().get(0).getIcon());
		weatherDTO.setWind(weather.getWind().speed);
		weatherDTO.setCity(weather.getName());
		return weatherDTO;
	}

}
