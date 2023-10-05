package backend.Marine.units.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import backend.Marine.units.entity.Ship;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Integer> {
	Optional<Ship> findByMmsi(int mmsi);

	@Query(value = "SELECT s FROM Ship s JOIN s.trackedBy u WHERE u.username =:username")
	List<Ship> findTrackedShipsByUsername(String username);

	@Query(value = "SELECT s FROM Ship s WHERE s.inArea = true")
	List<Ship> findAllInArea();

	@Query(value = "SELECT s FROM Ship s WHERE s.inArea = false")
	List<Ship> findAllOutArea();

	@Query(value = "SELECT s FROM Ship s WHERE s.active = true and s.inArea = true")
	List<Ship> findAllMovable();

	@Query(value = "SELECT s FROM Ship s WHERE s.active = false and s.inArea = true")
	List<Ship> findAllUnMovable();



}
