package io.github.nithril.yapetstore.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.hash.Hashing;
import io.github.nithril.yapetstore.domain.User;
import io.github.nithril.yapetstore.security.SecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Service;

/**
 * Created by nlabrot on 21/11/16.
 */
@Service
public class SecurityService {

  @Autowired
  @Qualifier("jwtSecretKey")
  private SecretKey jwtSecretKey;

  @Autowired
  @Qualifier("xsrfSecretKey")
  private SecretKey xsrfSecretKey;


  public String createJwtToken(User user) {
    return Jwts.builder()
        .setSubject(user.getUsername())
        .signWith(SignatureAlgorithm.HS512, jwtSecretKey.getValue())
        .compact();
  }

  public String createXsrfToken(String jwtToken) {
    return Jwts.builder()
        .setPayload(hashJwtToken(jwtToken))
        .signWith(SignatureAlgorithm.HS512, xsrfSecretKey.getValue())
        .compact();
  }


  public void checkXsrfToken(String xsrfToken, String jwtToken){

    //Check token
    Object body = Jwts.parser()
        .setSigningKey(xsrfSecretKey.getValue())
        .parse(xsrfToken).getBody();

    if (!body.equals(hashJwtToken(jwtToken))){
      throw new InvalidCookieException("Xsrf issue: mismatch");
    }
  }

  private String hashJwtToken(String jwtToken){
    return Hashing.sha256().hashString(jwtToken , UTF_8).toString();
  }
}
