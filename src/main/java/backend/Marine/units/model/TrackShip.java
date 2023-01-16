package backend.Marine.units.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class TrackShip {
	private String timeStamp;
	private Double sog;
	private Double rot;
	private Integer navstat;
	private Integer mmsi;
	private Double cog;
	private Geometry geometry;
	private Integer shipType;
	private String name;
	private Integer imo;
	private String callsign;
	private String country;
	private String eta;
	private String destination;
	private Boolean isSurvey;
	private Integer heading;
	private Double draught;
	private Integer a;
	private Integer b;
	private Integer c;
	private Integer d;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
