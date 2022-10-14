package hr.kraljic.web.module.notification.management.topic;

import hr.kraljic.web.exception.user.UserNotFoundApiException;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.module.core.user.repository.UserRepository;
import hr.kraljic.web.module.hydroponics.device.DeviceRepository;
import hr.kraljic.web.module.hydroponics.device.model.Device;
import hr.kraljic.web.module.hydroponics.event.DeviceAddedEvent;
import hr.kraljic.web.module.notification.NotificationPermission;
import hr.kraljic.web.module.notification.exception.InvalidTopicIdListApiException;
import hr.kraljic.web.module.notification.exception.TopicNotFoundApiException;
import hr.kraljic.web.module.notification.management.topic.event.TopicKeysResetEventPublisher;
import hr.kraljic.web.module.notification.management.topic.event.UserTopicsChangedEventPublisher;
import hr.kraljic.web.module.notification.management.topic.model.ManagementTopicDto;
import hr.kraljic.web.module.notification.management.topic.model.TopicManagementCommand;
import hr.kraljic.web.module.notification.management.topic.model.UserTopicDto;
import hr.kraljic.web.module.notification.topic.TopicRepository;
import hr.kraljic.web.module.notification.topic.model.Topic;
import hr.kraljic.web.util.RandomTokenGenerator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TopicsManagementServiceImpl implements TopicsManagementService {
    private final TopicRepository topicRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final RandomTokenGenerator firebaseTopicKeyGenerator;
    private final TopicKeysResetEventPublisher topicKeysResetEventPublisher;
    private final UserTopicsChangedEventPublisher userTopicsChangedEventPublisher;

    public TopicsManagementServiceImpl(TopicRepository topicRepository,
                                       DeviceRepository deviceRepository,
                                       UserRepository userRepository,
                                       RandomTokenGenerator firebaseTopicKeyGenerator,
                                       TopicKeysResetEventPublisher topicKeysResetEventPublisher,
                                       UserTopicsChangedEventPublisher userTopicsChangedEventPublisher) {
        this.topicRepository = topicRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.firebaseTopicKeyGenerator = firebaseTopicKeyGenerator;
        this.topicKeysResetEventPublisher = topicKeysResetEventPublisher;
        this.userTopicsChangedEventPublisher = userTopicsChangedEventPublisher;
    }

    @Override
    public List<ManagementTopicDto> getAllTopics() {
        List<Topic> topicList = topicRepository.findAll();

        return mapTopicListToDto(topicList);
    }

    @Override
    public List<UserTopicDto> getAllUsersTopic() {
        // Dohvati korisnike koji imaju dozvolu za primanje notifikacija
        return userRepository.findAll()
                .stream()
                .filter(u -> u.hasAuthority(NotificationPermission.ACCESS))
                .map(UserTopicDto::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ManagementTopicDto> getAllTopicsSubscribedByUser(String username) {
        ApplicationUser user = getUserByUsername(username);

        List<Topic> topicList = topicRepository.findAllFromUserByUsername(user.getUsername());

        return mapTopicListToDto(topicList);
    }

    @Override
    @Transactional
    public List<ManagementTopicDto> addUserToTopic(String username,
                                                   TopicManagementCommand topicManagementCommand) {
        ApplicationUser user = getUserByUsername(username);

        List<Topic> topics = getTopicsByIdList(topicManagementCommand.getTopics());

        // Obrisi prijasnje teme
        Set<Topic> oldUserTopics = Set.copyOf(user.getTopics());
        for (Topic topic : oldUserTopics) {
            topic.removeUser(user);
        }
        topicRepository.saveAll(oldUserTopics);

        // Dodaj nove teme
        for (Topic topic : topics) {
            topic.addUser(user);
        }
        topicRepository.saveAll(user.getTopics());

        userTopicsChangedEventPublisher.publishUserTopicsChangedEvent(user);

        return mapTopicListToDto(List.copyOf(user.getTopics()));
    }

    @Override
    public List<ManagementTopicDto> rebuildTopicList() {
        List<Device> devices = deviceRepository.findAll();

        for (Device device : devices) {
            boolean exists = topicRepository.existsByDevice(device);

            if (exists == false) {
                createDeviceTopic(device);
            }
        }

        return mapTopicListToDto(topicRepository.findAll());
    }

    @Override
    public void resetTopicKeys() {
        List<Topic> topics = topicRepository.findAll();
        for(Topic topic: topics) {
            topic.setTopicKey(firebaseTopicKeyGenerator.newRandomToken());
            topicRepository.save(topic);
        }

        topicKeysResetEventPublisher.publishTopicKeyResetEvent();
    }

    @EventListener
    public void onApplicationEvent(DeviceAddedEvent deviceAddedEvent) {
        createDeviceTopic(deviceAddedEvent.getDevice());
    }

    private Topic createDeviceTopic(Device device) {
        Topic topic = new Topic();
        topic.setDevice(device);
        topic.setTopicKey(firebaseTopicKeyGenerator.newRandomToken());

        return topicRepository.save(topic);
    }

    private List<ManagementTopicDto> mapTopicListToDto(List<Topic> topicList) {
        return topicList.stream()
                .map(ManagementTopicDto::map)
                .collect(Collectors.toList());
    }

    private Topic getTopicById(Integer topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundApiException(topicId));
    }

    private List<Topic> getTopicsByIdList(List<Integer> topicIdList) {
        List<Topic> topics = topicRepository.findAllById(topicIdList);
        if (topics.size() < topicIdList.size()) {
            throw new InvalidTopicIdListApiException();
        }

        return topics;
    }

    private ApplicationUser getUserByUsername(String username) {
        return userRepository.findOneByUsername(username)
                .orElseThrow(() -> new UserNotFoundApiException(username));
    }

}
