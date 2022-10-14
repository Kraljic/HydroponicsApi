package hr.kraljic.web.module.hydroponics.device;

import hr.kraljic.web.module.hydroponics.HydroponicsPermission;
import hr.kraljic.web.module.hydroponics.device.model.DeviceCommand;
import hr.kraljic.web.module.hydroponics.device.model.DeviceDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("hydroponic/api/device")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    @GetMapping
    @HydroponicsPermission.Read
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{deviceId}")
    @HydroponicsPermission.Read
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable("deviceId") Integer deviceId) {
        return ResponseEntity.ok(deviceService.getDeviceById(deviceId));
    }

    @PostMapping
    @HydroponicsPermission.WriteDevice
    public ResponseEntity<DeviceDto> createDevice(@Valid @RequestBody DeviceCommand deviceCommand) {
        return ResponseEntity.ok(deviceService.createDevice(deviceCommand));
    }

    @PutMapping("/{deviceId}")
    @HydroponicsPermission.WriteDevice
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable("deviceId") Integer deviceId,
                                                  @Valid @RequestBody DeviceCommand deviceCommand) {
        return ResponseEntity.ok(deviceService.updateDevice(deviceId, deviceCommand));
    }

    @DeleteMapping("/{deviceId}")
    @HydroponicsPermission.WriteDevice
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDevice(@PathVariable("deviceId") Integer deviceId) {
        deviceService.deleteDevice(deviceId);
    }
}
