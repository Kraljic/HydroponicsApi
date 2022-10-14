package hr.kraljic.web.module.hydroponics.device.model;

import lombok.Data;

@Data
public class DeviceDto {
    private Integer id;
    private String name;
    private String description;
    private String firmware;

    public static DeviceDto map(Device device) {
        DeviceDto dto = new DeviceDto();
        dto.setId(device.getId());
        dto.setName(device.getName());
        dto.setDescription(device.getDescription());
        dto.setFirmware(device.getFirmware());

        return dto;
    }
}
