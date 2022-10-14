package hr.kraljic.web.module.hydroponics.sensor.model;

import hr.kraljic.web.module.hydroponics.device.model.DeviceDto;
import hr.kraljic.web.module.hydroponics.sensor.value.SimpleValueDto;
import hr.kraljic.web.module.hydroponics.sensor.variable.SimpleVariableDto;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorTypeDto;
import lombok.Data;


@Data
public class SensorDto {
    private Integer id;
    private String name;
    private Boolean isManaged;

    private SimpleValueDto simpleValue;
    private SimpleVariableDto simpleVariable;
    private DeviceDto device;
    private SensorTypeDto sensorType;

    public static SensorDto map(Sensor sensor) {
        SensorDto dto = new SensorDto();
        dto.setId(sensor.getId());
        dto.setName(sensor.getName());
        dto.setIsManaged(sensor.getIsManaged());

        dto.setSimpleValue(
                SimpleValueDto.map(sensor.getSimpleValue())
        );
        dto.setSimpleVariable(
                SimpleVariableDto.map(sensor.getSimpleVariable())
        );
        dto.setDevice(
                DeviceDto.map(sensor.getDevice())
        );
        dto.setSensorType(
                SensorTypeDto.map(sensor.getSensorType())
        );

        return dto;
    }
}
