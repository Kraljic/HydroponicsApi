package hr.kraljic.web.module.hydroponics.device;

import hr.kraljic.web.module.hydroponics.event.DeviceEventPublisher;
import hr.kraljic.web.module.hydroponics.exception.DeviceDeleteIntegrityViolationApiException;
import hr.kraljic.web.module.hydroponics.exception.DeviceNotFoundApiException;
import hr.kraljic.web.module.hydroponics.device.model.Device;
import hr.kraljic.web.module.hydroponics.device.model.DeviceCommand;
import hr.kraljic.web.module.hydroponics.device.model.DeviceDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final DeviceEventPublisher deviceEventPublisher;

    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceEventPublisher deviceEventPublisher) {
        this.deviceRepository = deviceRepository;
        this.deviceEventPublisher = deviceEventPublisher;
    }

    @Override
    public List<DeviceDto> getAllDevices() {
        return deviceRepository.findAll()
                .stream()
                .map(DeviceDto::map)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceDto getDeviceById(Integer deviceId) {
        Device device = findDeviceById(deviceId);

        return DeviceDto.map(device);
    }

    @Override
    public DeviceDto createDevice(DeviceCommand deviceCommand) {
        Device device = mapToDevice(new Device(), deviceCommand);

        device = deviceRepository.save(device);

        deviceEventPublisher.publishDeviceAddedEvent(device);

        return DeviceDto.map(device);
    }

    @Override
    public DeviceDto updateDevice(Integer deviceId, DeviceCommand deviceCommand) {
        Device device = findDeviceById(deviceId);
        device = mapToDevice(device, deviceCommand);

        device = deviceRepository.save(device);

        return DeviceDto.map(device);
    }

    @Override
    public void deleteDevice(Integer deviceId) {
        Device device = findDeviceById(deviceId);

        try {
            deviceRepository.deleteById(device.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DeviceDeleteIntegrityViolationApiException(device.getName());
        }
    }

    private Device findDeviceById(Integer id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundApiException(id));
    }

    private Device mapToDevice(Device device, DeviceCommand deviceCommand) {
        device.setName(deviceCommand.getName());
        device.setDescription(deviceCommand.getDescription());
        device.setFirmware(deviceCommand.getFirmware());

        return device;
    }
}
