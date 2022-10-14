package hr.kraljic.web.module.hydroponics.sensor.variable;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SimpleVariableCommand {
    @NotNull(message = "Minimum value {valid.NotNull}")
    private Double minValue;

    @NotNull(message = "Maximum value {valid.NotNull}")
    private Double maxValue;
}
