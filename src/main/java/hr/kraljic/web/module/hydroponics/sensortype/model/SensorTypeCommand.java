package hr.kraljic.web.module.hydroponics.sensortype.model;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SensorTypeCommand {
    @NotEmpty(message = "Sensor type {valid.NotEmpty}")
    @Size(max = 45, message = "Sensor type {valid.Size.max}")
    private String type;

    @NotNull(message = "Measuring unit {valid.NotNull}")
    @Size(max = 45, message = "Sensor type {valid.Size.max}")
    private String measuringUnit;
}
