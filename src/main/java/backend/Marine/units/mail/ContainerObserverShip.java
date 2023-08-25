package backend.Marine.units.mail;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ContainerObserverShip {
	private Set<ShipMailObserver> observers;

	public ContainerObserverShip() {
		this.observers = new HashSet<ShipMailObserver>();

	}

	public void addObserverShip(ShipMailObserver observer) {
		observers.add(observer);
	}

	public void removeObserverShip(ShipMailObserver observer) {
		observers.remove(observer);
	}

	public Set<ShipMailObserver> getObserversShip() {
		return observers;
	}
}
