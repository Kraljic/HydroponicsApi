package hr.kraljic.web.module.notification.management.topic.model;

import hr.kraljic.web.module.notification.topic.model.TopicDto;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserTopicDto {
    private Integer id;
    private String username;
    private Boolean active;
    private List<TopicDto> topics;

    public static UserTopicDto map(ApplicationUser user) {
        UserTopicDto dto = new UserTopicDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setActive(user.isActive());

        dto.setTopics(
                user.getTopics()
                        .stream()
                        .map(TopicDto::map)
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
