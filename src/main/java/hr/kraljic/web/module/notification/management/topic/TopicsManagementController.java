package hr.kraljic.web.module.notification.management.topic;

import hr.kraljic.web.module.notification.management.topic.model.ManagementTopicDto;
import hr.kraljic.web.module.notification.management.topic.model.TopicManagementCommand;
import hr.kraljic.web.module.notification.management.topic.model.UserTopicDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("management/api/notification/topic")
public class TopicsManagementController {
    private final TopicsManagementService topicsManagementService;

    public TopicsManagementController(TopicsManagementService topicsManagementService) {
        this.topicsManagementService = topicsManagementService;
    }

    /**
     * Dohvaca sve teme na koje se korisnik moze pretplatiti
     *
     * @return
     */
    @GetMapping
    @TopicManagerPermission.Read
    public ResponseEntity<List<ManagementTopicDto>> findAll() {
        return ResponseEntity.ok(topicsManagementService.getAllTopics());
    }

    /**
     * Dohvaca sve korisnike koji mogu primati notifikacije
     *
     * @return
     */
    @GetMapping("/users")
    @TopicManagerPermission.Read
    public ResponseEntity<List<UserTopicDto>> findAllUsersTopic() {
        return ResponseEntity.ok(topicsManagementService.getAllUsersTopic());
    }

    /**
     * Dohvaca sve teme na koje je korisnik pretplacen
     *
     * @param username Korisnicko ime korisnika
     * @return
     */
    @GetMapping("/users/{username}")
    @TopicManagerPermission.Read
    public ResponseEntity<List<ManagementTopicDto>> findAllByUser(@PathVariable("username") String username) {
        return ResponseEntity.ok(topicsManagementService.getAllTopicsSubscribedByUser(username));
    }

    /**
     * Pretplacuje korisnika na teme
     *
     * @param username               Korisnicko ime korisnika
     * @param topicManagementCommand Id lista tema
     * @return Popis tema na koje je korisnik pretplacen
     */
    @PostMapping("/users/{username}")
    @TopicManagerPermission.Write
    public ResponseEntity<List<ManagementTopicDto>> addUserToTopics(@PathVariable("username") String username,
                                                                    @Valid @RequestBody TopicManagementCommand topicManagementCommand) {
        return ResponseEntity.ok(topicsManagementService.addUserToTopic(username, topicManagementCommand));
    }

    /**
     * Sinkronizira listu tema s dostupnim uredjajima
     *
     * @return Popis svih tema
     */
    @GetMapping("/rebuild")
    @TopicManagerPermission.Write
    public ResponseEntity<List<ManagementTopicDto>> rebuildTopicList() {
        return ResponseEntity.ok(topicsManagementService.rebuildTopicList());
    }


    /**
     * Stvara nove kljuceve za sve teme.
     */
    @GetMapping("/resetKeys")
    @TopicManagerPermission.Write
    @ResponseStatus(HttpStatus.OK)
    public void resetTopicKeys() {
        topicsManagementService.resetTopicKeys();
    }

}
