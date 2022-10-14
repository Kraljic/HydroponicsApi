package hr.kraljic.web.util.validator;

import hr.kraljic.web.config.PasswordPolicyProperties;
import hr.kraljic.web.util.MessagesUtil;
import org.passay.*;
import org.springframework.context.NoSuchMessageException;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class PasswordPolicyValidator implements ConstraintValidator<PasswordPolicyValidator.PasswordPolicy, String> {
    private static final String MESSAGES_PREFIX = "valid.password.";

    private final PasswordPolicyProperties props;

    private List<Rule> rules;

    public PasswordPolicyValidator(PasswordPolicyProperties passwordPolicyProperties) {
        this.props = passwordPolicyProperties;
        this.rules = new ArrayList<>();
    }

    /**
     * Inicializira validator
     */
    @Override
    public void initialize(final PasswordPolicyValidator.PasswordPolicy constraintAnnotation) {
        // Duzina lozinke
        if (props.getSize() != null)
            rules.add(new LengthRule(props.getSize().getMinLength(), props.getSize().getMaxLength()));
        // Mora sadrzavati velika slova
        if (props.getUppercaseCharacterRule() != null && props.getUppercaseCharacterRule() > 0)
            rules.add(new UppercaseCharacterRule(props.getUppercaseCharacterRule()));
        // Mora sadrzavati mala slova
        if (props.getLowercaseCharacterRule() != null && props.getLowercaseCharacterRule() > 0)
            rules.add(new LowercaseCharacterRule(props.getLowercaseCharacterRule()));
        // Mora sadrzavati brojeve
        if (props.getDigitCharacterRule() != null && props.getDigitCharacterRule() > 0)
            rules.add(new DigitCharacterRule(props.getDigitCharacterRule()));
        // Mora sadrzavati specialne znakove
        if (props.getSpecialCharacterRule() != null && props.getSpecialCharacterRule() > 0)
            rules.add(new SpecialCharacterRule(props.getSpecialCharacterRule()));
    }


    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        PasswordValidator validator = new PasswordValidator(rules);

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();

        StringBuilder messages = new StringBuilder();
        for (int i = 0; i < result.getDetails().size(); i++) {
            RuleResultDetail detail = result.getDetails().get(i);
            try {
                // Probaj dohvatiti lokaliziranu poruku
                messages.append(MessagesUtil.get(
                        MESSAGES_PREFIX + detail.getErrorCode(), detail.getValues()));
            } catch (NoSuchMessageException e) {
                // Dohvati default poruku
                messages.append(validator.getMessages(result).get(i));
            }
            messages.append("\n");
        }

        context.buildConstraintViolationWithTemplate(messages.toString()).addConstraintViolation();
        return false;
    }

    @Target({TYPE, FIELD, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Constraint(validatedBy = PasswordPolicyValidator.class)
    @Documented
    public @interface PasswordPolicy {
        String message() default "Not valid password.";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
