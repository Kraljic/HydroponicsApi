package hr.kraljic.web.module.hydroponics.sensor;

import hr.kraljic.web.module.hydroponics.sensor.model.SensorCommand;
import hr.kraljic.web.module.hydroponics.sensor.model.SensorDto;
import hr.kraljic.web.module.hydroponics.sensor.variable.SimpleVariableCommand;

import java.util.List;

public interface SensorService {
    List<SensorDto> getAllSensors();

    List<SensorDto> getAllSensorsByDeviceId(Integer deviceId);

    SensorDto getSensorById(Integer sensorId);

    SensorDto createSensor(SensorCommand sensorCommand, Integer deviceId, Integer sensorTypeId);

    SensorDto updateSensor(Integer sensorId, SensorCommand sensorCommand);

    SensorDto updateSimpleVariable(Integer sensorId, SimpleVariableCommand simpleVariableCommand);

    void deleteSensor(Integer sensorId);
}
