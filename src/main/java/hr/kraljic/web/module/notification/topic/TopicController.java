package hr.kraljic.web.module.notification.topic;

import hr.kraljic.web.module.notification.topic.model.TopicKeyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("notification/api/topic")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Dohvaca sve teme na koje je korisnik prijavljen, ako je token validan
     *
     * @param token token korisnika
     * @return Teme na koje je korisnik pretplacen
     */
    @GetMapping
    public ResponseEntity<List<TopicKeyDto>> getUserTopics(@RequestParam(name = "token") String token) {
        return ResponseEntity.ok(topicService.getSubscribedTopics(token));
    }
}
