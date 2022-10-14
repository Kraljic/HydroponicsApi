package hr.kraljic.web.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.kraljic.web.security.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Komponenta zasluzna za generiranje odgovora u slucaju neuspjesne autorizacije korisnika.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private HttpMessageConverter<String> messageConverter;

    private ObjectMapper mapper;

    public JwtAuthenticationEntryPoint(ObjectMapper mapper) {
        this.messageConverter = new StringHttpMessageConverter();
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        // Stvara novi API error objekt sa statusom 401 (Unauthorized) da se
        // korisniku da do znanja da nije autoriziran ili je token istekao
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        apiError.setMessage(e.getMessage());
        apiError.setDebugMessage(e.getMessage());

        ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
        outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);

        // Zapisi api error u tijelo odgovora
        messageConverter.write(mapper.writeValueAsString(apiError), MediaType.APPLICATION_JSON, outputMessage);
    }
}
