package hr.kraljic.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "password-policy")
@Validated
public class PasswordPolicyProperties {
    private PasswordSizeProperties size;

    @Min(0)
    @Max(64)
    private Integer lowercaseCharacterRule;

    @Min(0)
    @Max(64)
    private Integer uppercaseCharacterRule;

    @Min(0)
    @Max(64)
    private Integer digitCharacterRule;

    @Min(0)
    @Max(64)
    private Integer specialCharacterRule;

    @Data
    public static class PasswordSizeProperties {
        @NotNull
        @Min(1)
        @Max(64)
        private Integer minLength;

        @NotNull
        @Min(1)
        @Max(64)
        private Integer maxLength;
    }
}
