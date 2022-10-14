package hr.kraljic.web.module.hydroponics.sensor.variable;

import lombok.Data;

@Data
public class SimpleVariableDto {
    private Double minValue;
    private Double maxValue;

    public static SimpleVariableDto map(SimpleVariable simpleVariable) {
        if (simpleVariable == null) {
            return null;
        }

        SimpleVariableDto dto = new SimpleVariableDto();
        dto.setMinValue(simpleVariable.getMinValue());
        dto.setMaxValue(simpleVariable.getMaxValue());

        return dto;
    }
}
