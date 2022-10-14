package hr.kraljic.web.module.hydroponics.sensortype;

import hr.kraljic.web.module.hydroponics.sensortype.model.SensorType;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorTypeCommand;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorTypeDto;

import java.util.List;

public interface SensorTypeService {
    List<SensorTypeDto> getAllSensorTypes();

    SensorTypeDto getSensorTypeById(Integer sensorTypeId);

    SensorTypeDto createSensorType(SensorTypeCommand sensorTypeCommand);

    SensorTypeDto updateSensorType(Integer sensorTypeId, SensorTypeCommand sensorTypeCommand);

    void deleteSensorType(Integer sensorTypeId);
}
