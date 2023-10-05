package backend.Marine.units.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "ship_point")

public class Point {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private double xCoordinate, yCoordinate;
	private Date createDateTime;

	public Point(double xCoordinate, double yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	public Point(double xCoordinate, double yCoordinate, Date createDateTime) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.createDateTime = createDateTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(xCoordinate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yCoordinate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Point other = (Point) obj;
		if (Double.doubleToLongBits(xCoordinate) != Double.doubleToLongBits(other.xCoordinate))
			return false;
		return Double.compare(other.yCoordinate, yCoordinate) == 0;
	}

}
