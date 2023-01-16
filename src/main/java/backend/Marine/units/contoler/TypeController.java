package backend.Marine.units.contoler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.Marine.units.entity.Type;
import backend.Marine.units.repository.TypeRepository;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/type")
public class TypeController {

	private final TypeRepository typeRepository;

	public TypeController(TypeRepository typeRepository) {
		this.typeRepository = typeRepository;
	}

	@GetMapping
	@ApiOperation(value = "Get all type of ship")
	public ResponseEntity<List<Type>> geType() {
		List<Type> type = typeRepository.findAll();

		return new ResponseEntity<List<Type>>(type, HttpStatus.OK);
	}
}
