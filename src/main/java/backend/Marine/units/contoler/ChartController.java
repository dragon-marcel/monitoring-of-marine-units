package backend.Marine.units.contoler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.Marine.units.model.ChartShip;
import backend.Marine.units.service.ChartShipService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/chart")
public class ChartController {
	private final ChartShipService ChartShipService;

	public ChartController(ChartShipService ChartShipService) {
		this.ChartShipService = ChartShipService;
	}

	@GetMapping("/type")
	@ApiOperation(value = "Get weather by lat lon")
	public ResponseEntity<List<ChartShip>> getDataShipTypeChart() {
		List<ChartShip> charData = ChartShipService.getDataShipTypeChart();
		return new ResponseEntity<List<ChartShip>>(charData, HttpStatus.OK);
	}

	@GetMapping("/line")
	@ApiOperation(value = "Get weather by lat lon")
	public ResponseEntity<List<ChartShip>> getDataShipLineChart() {
		List<ChartShip> charData = ChartShipService.getCountShipByHour();
		return new ResponseEntity<List<ChartShip>>(charData, HttpStatus.OK);
	}

	@GetMapping("/amount/inarea")
	@ApiOperation(value = "Get weather by lat lon")
	public ResponseEntity<Integer> getCoutnShipInArea() {
		int amountShipInArea = ChartShipService.getCountShipInArea();
		return new ResponseEntity<Integer>(amountShipInArea, HttpStatus.OK);
	}

	@GetMapping("/amount/tracked/{username}")
	@ApiOperation(value = "Get weather by lat lon")
	public ResponseEntity<Integer> getCountShipTracked(@PathVariable String username) {
		int amountShipTracked = ChartShipService.getCountShipTracked(username);
		return new ResponseEntity<Integer>(amountShipTracked, HttpStatus.OK);
	}

	@GetMapping("/amount/outarea")
	@ApiOperation(value = "Get weather by lat lon")
	public ResponseEntity<Integer> getCoutnShipOutArea() {
		int amountShipOutArea = ChartShipService.getCountShipOutArea();
		return new ResponseEntity<Integer>(amountShipOutArea, HttpStatus.OK);
	}

	@GetMapping("/amount/movable")
	@ApiOperation(value = "Get weather by lat lon")
	public ResponseEntity<Integer> getCoutnShipMovable() {
		int amountShipOutArea = ChartShipService.getCountShipMovable();
		return new ResponseEntity<Integer>(amountShipOutArea, HttpStatus.OK);
	}

	@GetMapping("/amount/unmovable")
	@ApiOperation(value = "Get weather by lat lon")
	public ResponseEntity<Integer> getCoutnShipUnMovable() {
		int amountShipOutArea = ChartShipService.getCountShipUnMovable();
		return new ResponseEntity<Integer>(amountShipOutArea, HttpStatus.OK);
	}
}
