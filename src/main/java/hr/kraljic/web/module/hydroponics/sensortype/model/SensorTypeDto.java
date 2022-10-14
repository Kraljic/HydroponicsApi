package hr.kraljic.web.module.hydroponics.sensortype.model;

import lombok.Data;

@Data
public class SensorTypeDto {
    private Integer id;
    private String type;
    private String measuringUnit;

    public static SensorTypeDto map(SensorType sensorType) {
        SensorTypeDto dto = new SensorTypeDto();
        dto.setId(sensorType.getId());
        dto.setType(sensorType.getType());
        dto.setMeasuringUnit(sensorType.getMeasuringUnit());

        return dto;
    }
}
