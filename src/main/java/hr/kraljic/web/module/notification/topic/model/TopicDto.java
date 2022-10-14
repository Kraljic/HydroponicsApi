package hr.kraljic.web.module.notification.topic.model;

import lombok.Data;

@Data
public class TopicDto {
    private Integer id;
    private String name;

    public static TopicDto map(Topic topic) {
        TopicDto dto = new TopicDto();
        dto.setId(topic.getId());
        dto.setName(topic.getDevice().getName());

        return dto;
    }
}
