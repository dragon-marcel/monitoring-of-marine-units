package backend.Marine.units.controller;

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
	private final ChartShipService chartShipService;

	public ChartController(ChartShipService chartShipService) {
		this.chartShipService = chartShipService;
	}

	@GetMapping("/type")
	@ApiOperation("Get data for ship type chart")
	public ResponseEntity<List<ChartShip>> getDataShipTypeChart() {
		List<ChartShip> charData = chartShipService.getDataShipTypeChart();
		return new ResponseEntity<List<ChartShip>>(charData, HttpStatus.OK);
	}

	@GetMapping("/line")
	@ApiOperation("Get data for ship line chart")
	public ResponseEntity<List<ChartShip>> getDataShipLineChart() {
		List<ChartShip> charData = chartShipService.getCountShipByHour();
		return new ResponseEntity<List<ChartShip>>(charData, HttpStatus.OK);
	}

	@GetMapping("/amount/in_area")
	@ApiOperation("Get count of ships in the specified area")
	public ResponseEntity<Integer> getCoutnShipInArea() {
		int amountShipInArea = chartShipService.getCountShipInArea();
		return new ResponseEntity<Integer>(amountShipInArea, HttpStatus.OK);
	}

	@GetMapping("/amount/tracked/{username}")
	@ApiOperation("Get count of ships tracked by username")
	public ResponseEntity<Integer> getCountShipTracked(@PathVariable String username) {
		int amountShipTracked = chartShipService.getCountShipTracked(username);
		return new ResponseEntity<Integer>(amountShipTracked, HttpStatus.OK);
	}

	@GetMapping("/amount/out_area")
	@ApiOperation("Get count of ships out of the specified area")
	public ResponseEntity<Integer> getCoutnShipOutArea() {
		int amountShipOutArea = chartShipService.getCountShipOutArea();
		return new ResponseEntity<Integer>(amountShipOutArea, HttpStatus.OK);
	}

	@GetMapping("/amount/movable")
	@ApiOperation("Get count of movable ships")
	public ResponseEntity<Integer> getCoutnShipMovable() {
		int amountShipOutArea = chartShipService.getCountShipMovable();
		return new ResponseEntity<Integer>(amountShipOutArea, HttpStatus.OK);
	}

	@GetMapping("/amount/unmovable")
	@ApiOperation("Get count of unmovable ships")
	public ResponseEntity<Integer> getCoutnShipUnMovable() {
		int amountShipOutArea = chartShipService.getCountShipUnMovable();
		return new ResponseEntity<Integer>(amountShipOutArea, HttpStatus.OK);
	}
}
