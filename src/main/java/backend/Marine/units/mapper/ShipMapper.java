package backend.Marine.units.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import backend.Marine.units.entity.Ship;
import backend.Marine.units.model.TrackShip;

@Service
public interface ShipMapper {
	public static final ShipMapper INSTANCE = Mappers.getMapper(ShipMapper.class);

	Ship toShip(TrackShip track);
}
