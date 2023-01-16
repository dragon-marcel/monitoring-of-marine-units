package backend.Marine.units.model.weather;

import java.util.ArrayList;

import backend.Marine.units.entity.Main;
import lombok.Data;

@Data
public class WeatherConditions {
	public Coord coord;
	public ArrayList<Weather> weather;
	public String base;
	public Main main;
	public int visibility;
	public Wind wind;
	public Clouds clouds;
	public int dt;
	public Sys sys;
	public int timezone;
	public int id;
	public String name;
	public int cod;
}
