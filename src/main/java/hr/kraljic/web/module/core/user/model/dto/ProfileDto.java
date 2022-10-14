package hr.kraljic.web.module.core.user.model.dto;

import hr.kraljic.web.module.core.user.model.Profile;
import lombok.Data;

@Data
public class ProfileDto {
    private String firstName;
    private String lastName;

    /**
     * Mapira profile objekt na profile dto objekt
     * @param profile Profile korisnika
     * @return mapirani profile dto objekt
     */
    public static ProfileDto map(Profile profile) {
        ProfileDto profileDto = new ProfileDto();

        if (profile != null) {
            profileDto.setFirstName(profile.getFirstName());
            profileDto.setLastName(profile.getLastName());
        }

        return profileDto;
    }
}
