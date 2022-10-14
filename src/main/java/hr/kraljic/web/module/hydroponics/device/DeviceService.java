package hr.kraljic.web.module.hydroponics.device;

import hr.kraljic.web.module.hydroponics.device.model.DeviceCommand;
import hr.kraljic.web.module.hydroponics.device.model.DeviceDto;

import java.util.List;

public interface DeviceService {
    List<DeviceDto> getAllDevices();

    DeviceDto getDeviceById(Integer deviceId);

    DeviceDto createDevice(DeviceCommand deviceCommand);

    DeviceDto updateDevice(Integer deviceId, DeviceCommand deviceCommand);

    void deleteDevice(Integer deviceId);
}
