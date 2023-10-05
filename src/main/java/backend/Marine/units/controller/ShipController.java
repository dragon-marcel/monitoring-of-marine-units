package backend.Marine.units.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.Marine.units.entity.Ship;
import backend.Marine.units.repository.ShipRepository;
import backend.Marine.units.service.ShipService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping(value = "/api/ships")
public class ShipController {
	private final ShipRepository shipRepository;
	private final ShipService shipService;
	@Autowired
	public ShipController(ShipRepository shipRepository, ShipService shipService) {
		this.shipRepository = shipRepository;
		this.shipService = shipService;
	}

	@ApiOperation(value = "Get all ships")
	@GetMapping
	public ResponseEntity<List<Ship>> getShips() {
		List<Ship> ships = shipRepository.findAll();
		log.info("new customer registration {}",ships);
		return new ResponseEntity<List<Ship>>(ships, HttpStatus.OK);
	}

	@GetMapping(value = "/area")
	@ApiOperation(value = "Get all ships in area")
	public ResponseEntity<List<Ship>> getShipsInArea() {
		List<Ship> ships = shipRepository.findAllInArea();

		return new ResponseEntity<List<Ship>>(ships, HttpStatus.OK);
	}

	@PostMapping("/username/{username}/mmsi/{mmsi}/add")
	@ApiOperation(value = "Add tracked ship")
	public ResponseEntity<?> trackShip(@PathVariable int mmsi, @PathVariable String username) {

		shipService.addTrackedShip(username, mmsi);
		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@PostMapping("/username/{username}/mmsi/{mmsi}/remove")
	@ApiOperation(value = "Remove tracked ship")
	public ResponseEntity<?> removeTrackedShip(@PathVariable int mmsi, @PathVariable String username) {
		shipService.deleteTrackedShip(username, mmsi);
		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@GetMapping("/{username}")
	@ApiOperation(value = "Get tracked ships by username")
	public ResponseEntity<List<Ship>> getTrackedShipsByUsername(@PathVariable String username) {
		List<Ship> trackedShips = shipRepository.findTrackedShipsByUsername(username);
		return new ResponseEntity<List<Ship>>(trackedShips, HttpStatus.OK);
	}

}
