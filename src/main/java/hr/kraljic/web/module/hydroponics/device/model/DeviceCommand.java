package hr.kraljic.web.module.hydroponics.device.model;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class DeviceCommand {
    @NotEmpty(message = "Name {valid.NotEmpty}")
    @Size(min = 3, max = 45, message = "Firmware {valid.Size.minmax}")
    private String name;

    @NotEmpty(message = "Description {valid.NotEmpty}")
    @Size(max = 255, message = "Firmware {valid.Size.max}")
    private String description;

    @NotEmpty(message = "Firmware {valid.NotEmpty}")
    @Size(min = 2, max = 45, message = "Firmware {valid.Size.minmax}")
    private String firmware;
}
