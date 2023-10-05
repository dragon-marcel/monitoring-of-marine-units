package backend.Marine.units.controller;

import backend.Marine.units.entity.Role;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {
    @GetMapping
    @ApiOperation("Get user roles")
    public ResponseEntity<List<?>> getUsersRole() {

        return new ResponseEntity<List<?>>(Arrays.asList(Role.values()), HttpStatus.OK);
    }
}
