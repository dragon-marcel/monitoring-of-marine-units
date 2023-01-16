package backend.Marine.units.mapper;

import org.mapstruct.factory.Mappers;

import backend.Marine.units.model.weather.WeatherConditions;
import dto.WeatherDTO;

public interface WeatherMapper {
	public static final WeatherMapper INSTANCE = Mappers.getMapper(WeatherMapper.class);

	WeatherDTO toWeatherDTO(WeatherConditions weather);

}
