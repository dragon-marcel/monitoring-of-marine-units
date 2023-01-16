package backend.Marine.units.contoler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.Marine.units.entity.Ship;
import backend.Marine.units.repository.ShipRepository;
import backend.Marine.units.service.ShipService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/ships")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShipController {
	private final ShipRepository shipRepository;
	private final ShipService shipService;

	public ShipController(ShipRepository shipRepository, ShipService shipService) {
		this.shipRepository = shipRepository;
		this.shipService = shipService;
	}

	@GetMapping
	@ApiOperation(value = "Get all ships")
	public ResponseEntity<List<Ship>> getShips() {
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
	@ApiOperation(value = "Delete tracked ship")
	public ResponseEntity<?> deleteTrackedShip(@PathVariable int mmsi, @PathVariable String username) {
		shipService.deleteTrackedShip(username, mmsi);
		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@GetMapping("/{username}")
	@ApiOperation(value = "Get tracked ships by username")
	public ResponseEntity<List<Ship>> getTracketShipsByUsername(@PathVariable String username) {
		List<Ship> ships = shipRepository.findTrackedShipsByUsername(username);
		System.out.print("Get tracked Ship by username" + username + "results:" + ships.size());
		return new ResponseEntity<List<Ship>>(ships, HttpStatus.OK);
	}

}
