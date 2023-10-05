package backend.Marine.units.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ships")
public class Ship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@JoinColumn(name = "type_id")
	private Type type;
	private Double distance;
	private int speed;
	@ManyToMany()
	@JoinTable(
			name = "ships_tracked_by", // Nazwa tabeli łączącej
			joinColumns = @JoinColumn(name = "ship_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
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
		return "Ship{" +
				"id=" + id +
				", mmsi=" + mmsi +
				", name='" + name + '\'' +
				", currentPoint=" + currentPoint +
				", country='" + country + '\'' +
				", destination='" + destination + '\'' +
				", img='" + img + '\'' +
				", track=" + track +
				", inArea=" + inArea +
				", active=" + active +
				", type=" + type +
				", distance=" + distance +
				", speed=" + speed +
				", trackedBy=" + trackedBy +
				'}';
	}
}
