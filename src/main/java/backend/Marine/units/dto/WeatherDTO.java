package backend.Marine.units.dto;

import lombok.Data;

@Data
public class WeatherDTO {
	private double temp;
	private String description;
	private String icon;
	private double wind;
	private String city;
}
