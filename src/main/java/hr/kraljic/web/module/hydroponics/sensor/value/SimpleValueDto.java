package hr.kraljic.web.module.hydroponics.sensor.value;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleValueDto {
    private Integer id;
    private Double value;
    private LocalDateTime updatedAt;

    public static SimpleValueDto map(SimpleValue simpleValue) {
        if (simpleValue == null) {
            return null;
        }

        SimpleValueDto dto = new SimpleValueDto();
        dto.setId(simpleValue.getId());
        dto.setValue(simpleValue.getValue());
        dto.setUpdatedAt(simpleValue.getUpdatedAt());

        return dto;
    }
}
