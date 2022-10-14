package hr.kraljic.web.module.notification.topic;

import hr.kraljic.web.module.hydroponics.device.model.Device;
import hr.kraljic.web.module.notification.topic.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    /**
     * Pronadji sve teme na koje se je korisnik pretplatio.
     *
     * @param username Korisnicko ime korisnika
     * @return Teme na koje se je korisnik pretplatio.
     */
    @Query("SELECT DISTINCT t FROM Topic t INNER JOIN t.subscribedUsers su WHERE su.username = :username")
    List<Topic> findAllFromUserByUsername(String username);

    /**
     * Provjeri da li postoji tema za taj uredjaj.
     * @param device Uredjaj za koji se zeli provjeriti postoji li tema.
     * @return {@code true} ako postoji tema za taj uredjaj
     */
    boolean existsByDevice(Device device);
}
