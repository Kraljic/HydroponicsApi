package hr.kraljic.web.module.notification.management.topic;

import hr.kraljic.web.module.notification.management.topic.model.ManagementTopicDto;
import hr.kraljic.web.module.notification.management.topic.model.TopicManagementCommand;
import hr.kraljic.web.module.notification.management.topic.model.UserTopicDto;

import java.util.List;

public interface TopicsManagementService {

    /**
     * Popis svih tema.
     *
     * @return
     */
    List<ManagementTopicDto> getAllTopics();

    /**
     * Popis svih korisnika.
     *
     * @return
     */
    List<UserTopicDto> getAllUsersTopic();

    /**
     * Popis svih tema na koje je pretplacen korisnik.
     *
     * @param username Korisnicko ime korisnika
     * @return Pipis tema
     */
    List<ManagementTopicDto> getAllTopicsSubscribedByUser(String username);

    /**
     * Dodaj korisniku popis tema na koje ce biti pretplaceni, sve ostale se uklanjaju
     *
     * @param username               Korisnicko ime korisnika kojem se zele dodjeliti teme
     * @param topicManagementCommand Id lista tema koje se zele dodjeliti korisniku
     * @return Lista tema koje pripadaju korisniku
     */
    List<ManagementTopicDto> addUserToTopic(String username, TopicManagementCommand topicManagementCommand);

    /**
     * Azurira listu tema da se podudaraju listi uredjaja.
     *
     * @return Sve dostupne teme
     */
    List<ManagementTopicDto> rebuildTopicList();

    /**
     * Stvara nove kljuceve za sve teme.
     */
    void resetTopicKeys();
}
