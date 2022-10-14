package hr.kraljic.web.module.hydroponics.sensor;

import hr.kraljic.web.module.hydroponics.device.DeviceRepository;
import hr.kraljic.web.module.hydroponics.exception.DeviceNotFoundApiException;
import hr.kraljic.web.module.hydroponics.device.model.Device;
import hr.kraljic.web.module.hydroponics.exception.IllegalSensorVariableApiException;
import hr.kraljic.web.module.hydroponics.exception.SensorNotFoundApiException;
import hr.kraljic.web.module.hydroponics.sensor.model.Sensor;
import hr.kraljic.web.module.hydroponics.sensor.model.SensorCommand;
import hr.kraljic.web.module.hydroponics.sensor.model.SensorDto;
import hr.kraljic.web.module.hydroponics.sensor.variable.SimpleVariable;
import hr.kraljic.web.module.hydroponics.sensor.variable.SimpleVariableCommand;
import hr.kraljic.web.module.hydroponics.sensortype.SensorTypeRepository;
import hr.kraljic.web.module.hydroponics.exception.SensorTypeNotFoundApiException;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorServiceImpl implements SensorService {
    private final SensorRepository sensorRepository;
    private final DeviceRepository deviceRepository;
    private final SensorTypeRepository sensorTypeRepository;

    public SensorServiceImpl(SensorRepository sensorRepository, DeviceRepository deviceRepository, SensorTypeRepository sensorTypeRepository) {
        this.sensorRepository = sensorRepository;
        this.deviceRepository = deviceRepository;
        this.sensorTypeRepository = sensorTypeRepository;
    }

    @Override
    public List<SensorDto> getAllSensors() {
        return sensorRepository.findAll()
                .stream()
                .map(SensorDto::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<SensorDto> getAllSensorsByDeviceId(Integer deviceId) {
        return sensorRepository.findAllByDevice_Id(deviceId)
                .stream()
                .map(SensorDto::map)
                .collect(Collectors.toList());
    }

    @Override
    public SensorDto getSensorById(Integer sensorId) {
        Sensor sensor = findSensorById(sensorId);

        return SensorDto.map(sensor);
    }

    @Override
    public SensorDto createSensor(SensorCommand sensorCommand, Integer deviceId, Integer sensorTypeId) {
        Device device = findDeviceById(deviceId);
        SensorType sensorType = findSensorTypeById(sensorTypeId);

        Sensor sensor = mapToSensor(new Sensor(), sensorCommand);
        sensor.setDevice(device);
        sensor.setSensorType(sensorType);

        sensor = sensorRepository.save(sensor);

        return SensorDto.map(sensor);
    }

    @Override
    public SensorDto updateSensor(Integer sensorId, SensorCommand sensorCommand) {
        Sensor sensor = findSensorById(sensorId);
        sensor = mapToSensor(sensor, sensorCommand);

        sensor = sensorRepository.save(sensor);

        return SensorDto.map(sensor);
    }

    @Override
    public void deleteSensor(Integer sensorId) {
        Sensor sensor = findSensorById(sensorId);
        sensorRepository.deleteById(sensor.getId());
    }

    @Override
    public SensorDto updateSimpleVariable(Integer sensorId, SimpleVariableCommand simpleVariableCommand) {
        Sensor sensor = findSensorById(sensorId);
        SimpleVariable simpleVariable = mapToSimpleVariable(sensor, simpleVariableCommand);

        sensor.setSimpleVariable(simpleVariable);

        sensor = sensorRepository.save(sensor);

        return SensorDto.map(sensor);
    }

    private Sensor findSensorById(Integer id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new SensorNotFoundApiException(id));
    }

    private Device findDeviceById(Integer id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundApiException(id));
    }

    private SensorType findSensorTypeById(Integer id) {
        return sensorTypeRepository.findById(id)
                .orElseThrow(() -> new SensorTypeNotFoundApiException(id));
    }

    private Sensor mapToSensor(Sensor sensor, SensorCommand sensorCommand) {
        sensor.setName(sensorCommand.getName());
        sensor.setIsManaged(sensorCommand.getIsManaged());

        return sensor;
    }

    private SimpleVariable mapToSimpleVariable(Sensor sensor, SimpleVariableCommand simpleVariableCommand) {
        SimpleVariable simpleVariable = new SimpleVariable();
        simpleVariable.setSensor(sensor);

        if (simpleVariableCommand.getMaxValue() <  simpleVariableCommand.getMinValue()) {
            throw new IllegalSensorVariableApiException();
        }

        simpleVariable.setId(sensor.getId());
        simpleVariable.setMinValue(simpleVariableCommand.getMinValue());
        simpleVariable.setMaxValue(simpleVariableCommand.getMaxValue());

        return simpleVariable;
    }
}
