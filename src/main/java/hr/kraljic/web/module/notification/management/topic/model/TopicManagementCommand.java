package hr.kraljic.web.module.notification.management.topic.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TopicManagementCommand {
    @NotNull(message = "Topic id list {valid.NotNull}")
    List<Integer> topics;
}
