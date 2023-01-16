package backend.Marine.units.model;

import java.util.List;

import lombok.Data;

@Data
public class Geometry {

	private String type;
	private List<Double> coordinates = null;

}
