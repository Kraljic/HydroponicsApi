package hr.kraljic.web.config;

import hr.kraljic.web.security.SecurityUtils;
import hr.kraljic.web.util.RandomTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") // Definira ime bean-a koji dohvaca auditora
public class Config implements WebMvcConfigurer {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> SecurityUtils.getCurrentUserUsername();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Predefinirani jezik za lokalizaciju, ako nije drugačije navedeno
     *
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.US);
        return sessionLocaleResolver;
    }

    /**
     * Postavlja jezik lokalizacije na odabrani jezik korisnika
     *
     * @return
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    /**
     * Sluizi za ucitavanje lokaliziranih poruka
     *
     * @return Izvor lokaliziranih poruka
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Sluzi za asinkrono izvrsavanje dogadaja
     *
     * @return
     */
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster =
                new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }

    /**
     * Sluzi za lokalizaciju validacijskih poruka. Poruke koje se ispisuju prilikom neispravnog korisnikovog unosa.
     *
     * @param messageSource
     * @return
     */
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }


    /**
     * Sluzi za generiranje nasumicnih alfanumerickih vrijednosti koje predstavljaju refreshtoken
     *
     * @param length Broj znakova u refresh tokenu
     * @return Nasumicni niz znakova
     */
    @Bean
    public RandomTokenGenerator refreshTokenGenerator(@Value("${refresh-token-generator.length}") int length) {
        return new RandomTokenGenerator(length);
    }

    /**
     * Sluzi za generiranje nasumicnih alfanumerickih vrijednosti koje predstavljaju notification access token
     *
     * @param length Broj znakova u notification access tokenu
     * @return Nasumicni niz znakova
     */
    @Bean
    public RandomTokenGenerator notificationAccessTokenGenerator(@Value("${notification-access-token-generator.length}") int length) {
        return new RandomTokenGenerator(length);
    }

    /**
     * Sluzi za generiranje nasumicnih alfanumerickih vrijednosti koje predstavljaju kljuc za primanje firebase notifikacija
     *
     * @param length Broj znakova u kljucu za primanje firebase notifikacija
     * @return Nasumicni niz znakova
     */
    @Bean
    public RandomTokenGenerator firebaseTopicKeyGenerator(@Value("${firebase-notification-key.length}") int length) {
        return new RandomTokenGenerator(length);
    }
}
