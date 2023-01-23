package backend.Marine.units.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ships")
public class Ship {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	private int mmsi;
	private String name;
	@OneToOne(cascade = CascadeType.ALL)
	private Point currentPoint;
	private String country;
	private String destination;
	private String img;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Point> track = new ArrayList<Point>();
	private boolean inArea;
	private boolean active;
	@OneToOne
	private Type type;
	private Double distance;
	private int speed;
	@ManyToMany()
	@JsonIgnore
	private Set<User> trackedBy = new HashSet<>();

	public Ship(Integer mmsi, String name, String country, String destination, Type type) {
		this.mmsi = mmsi;
		this.name = name;
		this.country = country;
		this.destination = destination;
		this.type = type;
	}

	public void addPoint(Point point) {
		this.track.add(point);
	}

	public void addUser(User user) {
		this.trackedBy.add(user);
	}

	@Override
	public String toString() {
		return "Ship [id=" + id + ", mmsi=" + mmsi + ", name=" + name + ", shipType=" + type.getDescription()
				+ ", currentPoint=" + currentPoint + ", country=" + country + ", destination=" + destination
				+ ", track=" + track + ", inArea=" + inArea + ", active=" + active + "]";
	}

}
