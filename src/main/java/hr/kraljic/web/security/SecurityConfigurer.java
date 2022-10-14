package hr.kraljic.web.security;

import hr.kraljic.web.module.core.authentication.service.ApplicationUserService;
import hr.kraljic.web.module.hydroponics.HydroponicsPermission;
import hr.kraljic.web.module.notification.NotificationPermission;
import hr.kraljic.web.module.notification.management.topic.TopicManagerPermission;
import hr.kraljic.web.module.management.role.RoleManagerPermission;
import hr.kraljic.web.module.management.user.UserManagerPermission;
import hr.kraljic.web.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final ApplicationUserService applicationUserService;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtFilter jwtFilter;

    public SecurityConfigurer(ApplicationUserService applicationUserService, AuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtFilter jwtFilter) {
        this.applicationUserService = applicationUserService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/auth/validate-token").authenticated()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/notification/api/topic").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/management/api/user/**").hasAuthority(UserManagerPermission.ACCESS)
                .antMatchers("/management/api/role/**").hasAuthority(RoleManagerPermission.ACCESS)
                .antMatchers("/management/api/notification/topic/**").hasAuthority(TopicManagerPermission.ACCESS)
                .antMatchers("/hydroponic/api/**").hasAuthority(HydroponicsPermission.ACCESS)
                .antMatchers("/notification/api/**").hasAuthority(NotificationPermission.ACCESS)
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.applicationUserService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(@Value("${cors.allowed-origins}") String[] allowedOrigins) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOrigins(allowedOrigins);
            }
        };
    }
}
