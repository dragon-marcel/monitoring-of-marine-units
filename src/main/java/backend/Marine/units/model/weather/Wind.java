package backend.Marine.units.model.weather;

import lombok.Data;

@Data
public class Wind {
	public double speed;
	public int deg;
	public double gust;
}
