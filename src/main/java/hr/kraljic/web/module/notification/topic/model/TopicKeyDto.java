package hr.kraljic.web.module.notification.topic.model;

import lombok.Data;

@Data
public class TopicKeyDto {
    private Integer id;
    private String name;
    private String topicKey;

    public static TopicKeyDto map(Topic topic) {
        TopicKeyDto dto = new TopicKeyDto();
        dto.setId(topic.getId());
        dto.setName(topic.getDevice().getName());
        dto.setTopicKey(topic.getTopicKey());

        return dto;
    }
}
