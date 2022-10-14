package hr.kraljic.web.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class StringHash {

    private MessageDigest md;

    public StringHash(@Value("${token-hash-algorithm}") String hashAlgorithm) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance(hashAlgorithm);
    }

    public String getHash(String input) {
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        String encodedB64 = Base64.getEncoder().encodeToString(digest);

        return encodedB64;
    }
}
