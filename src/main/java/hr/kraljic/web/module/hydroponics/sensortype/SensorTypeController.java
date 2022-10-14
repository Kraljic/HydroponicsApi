package hr.kraljic.web.module.hydroponics.sensortype;

import hr.kraljic.web.module.hydroponics.HydroponicsPermission;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorTypeCommand;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorTypeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("hydroponic/api/sensorType")
public class SensorTypeController {
    private final SensorTypeService sensorTypeService;

    public SensorTypeController(SensorTypeService sensorTypeService) {
        this.sensorTypeService = sensorTypeService;
    }

    @GetMapping
    @HydroponicsPermission.Read
    public ResponseEntity<List<SensorTypeDto>> getAllSensorTypes() {
        return ResponseEntity.ok(sensorTypeService.getAllSensorTypes());
    }

    @GetMapping("/{sensorTypeId}")
    @HydroponicsPermission.Read
    public ResponseEntity<SensorTypeDto> getSensorTypeById(@PathVariable("sensorTypeId") Integer sensorTypeId) {
        return ResponseEntity.ok(sensorTypeService.getSensorTypeById(sensorTypeId));
    }

    @PostMapping
    @HydroponicsPermission.WriteSensorType
    public ResponseEntity<SensorTypeDto> createSensorType(@Valid @RequestBody SensorTypeCommand sensorTypeCommand) {
        return ResponseEntity.ok(sensorTypeService.createSensorType(sensorTypeCommand));
    }

    @PutMapping("/{sensorTypeId}")
    @HydroponicsPermission.WriteSensorType
    public ResponseEntity<SensorTypeDto> updateSensorType(@PathVariable("sensorTypeId") Integer sensorTypeId,
                                                          @Valid @RequestBody SensorTypeCommand sensorTypeCommand) {
        return ResponseEntity.ok(sensorTypeService.updateSensorType(sensorTypeId, sensorTypeCommand));
    }

    @DeleteMapping("/{sensorTypeId}")
    @HydroponicsPermission.WriteSensorType
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSensorType(@PathVariable("sensorTypeId") Integer sensorTypeId) {
        sensorTypeService.deleteSensorType(sensorTypeId);
    }
}
