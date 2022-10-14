package hr.kraljic.web.module.notification.management.topic.model;

import hr.kraljic.web.module.hydroponics.device.model.DeviceDto;
import hr.kraljic.web.module.notification.topic.model.Topic;
import hr.kraljic.web.module.core.user.model.dto.SimpleUserDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ManagementTopicDto {
    private Integer id;
    private String name;
    private DeviceDto device;
    private List<SimpleUserDto> users;

    public static ManagementTopicDto map(Topic topic) {
        ManagementTopicDto dto = new ManagementTopicDto();
        dto.setId(topic.getId());
        dto.setName(topic.getDevice().getName());
        dto.setDevice(DeviceDto.map(topic.getDevice()));

        dto.setUsers(
                topic.getSubscribedUsers()
                        .stream()
                        .map(SimpleUserDto::map)
                        .collect(Collectors.toList())
        );

        return dto;
    }

}
