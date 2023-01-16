package backend.Marine.units.mail;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ContainerObserverShip {
	private Set<ObserverShip> observers;

	public ContainerObserverShip() {
		this.observers = new HashSet<ObserverShip>();

	}

	public void addObserverShip(ObserverShip observer) {
		observers.add(observer);
	}

	public void removeObserverShip(ObserverShip observer) {
		observers.remove(observer);
	}

	public Set<ObserverShip> getObserversShip() {
		return observers;
	}
}
