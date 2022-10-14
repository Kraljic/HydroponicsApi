package hr.kraljic.web.module.hydroponics.sensor;

import hr.kraljic.web.module.hydroponics.HydroponicsPermission;
import hr.kraljic.web.module.hydroponics.sensor.model.SensorCommand;
import hr.kraljic.web.module.hydroponics.sensor.model.SensorDto;
import hr.kraljic.web.module.hydroponics.sensor.variable.SimpleVariableCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("hydroponic/api/sensor")
public class SensorController {
    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    @HydroponicsPermission.Read
    public ResponseEntity<List<SensorDto>> getAllSensors() {
        return ResponseEntity.ok(sensorService.getAllSensors());
    }

    @GetMapping("/{sensorId}")
    @HydroponicsPermission.Read
    public ResponseEntity<SensorDto> getSensorById(@PathVariable("sensorId") Integer sensorId) {
        return ResponseEntity.ok(sensorService.getSensorById(sensorId));
    }

    @GetMapping("/device/{deviceId}")
    @HydroponicsPermission.Read
    public ResponseEntity<List<SensorDto>> getAllSensorsByDeviceId(@PathVariable("deviceId") Integer deviceId) {
        return ResponseEntity.ok(sensorService.getAllSensorsByDeviceId(deviceId));
    }

    @PostMapping
    @HydroponicsPermission.WriteSensor
    public ResponseEntity<SensorDto> createSensor(@Valid @RequestBody SensorCommand sensorCommand,
                                                  @RequestParam("deviceId") Integer deviceId,
                                                  @RequestParam("sensorTypeId") Integer sensorTypeId) {
        return ResponseEntity.ok(sensorService.createSensor(sensorCommand, deviceId, sensorTypeId));
    }

    @PutMapping("/{sensorId}")
    @HydroponicsPermission.WriteSensor
    public ResponseEntity<SensorDto> updateSensor(@PathVariable("sensorId") Integer sensorId,
                                                  @Valid @RequestBody SensorCommand sensorCommand) {
        return ResponseEntity.ok(sensorService.updateSensor(sensorId, sensorCommand));
    }

    @PutMapping("/variable/{sensorId}")
    @HydroponicsPermission.WriteSensorVariable
    public ResponseEntity<SensorDto> updateSimpleVariable(@PathVariable("sensorId") Integer sensorId,
                                                          @Valid @RequestBody SimpleVariableCommand simpleVariableCommand) {
        return ResponseEntity.ok(sensorService.updateSimpleVariable(sensorId, simpleVariableCommand));
    }

    @DeleteMapping("/{sensorId}")
    @HydroponicsPermission.WriteSensor
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSensor(@PathVariable("sensorId") Integer sensorId) {
        sensorService.deleteSensor(sensorId);
    }
}
