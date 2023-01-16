package backend.Marine.units.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.Marine.units.entity.Type;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {

}
