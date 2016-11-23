package io.github.nithril.yapetstore.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.hash.Hashing;
import io.github.nithril.yapetstore.domain.User;
import io.github.nithril.yapetstore.security.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
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


  /**
   * Create the jwt token
   *
   * @param user
   * @return
   */
  public String createJwtToken(User user) {
    return Jwts.builder()
        .setSubject(user.getUsername())
        .signWith(SignatureAlgorithm.HS512, jwtSecretKey.getKey())
        .compact();
  }

  /**
   * Create the xsrf token which is a signed hash of the jwt token
   *
   * @param jwtToken
   * @return
   */
  public String createXsrfToken(String jwtToken) {
    return Jwts.builder()
        .setPayload(hashJwtToken(jwtToken))
        .signWith(SignatureAlgorithm.HS512, xsrfSecretKey.getKey())
        .compact();
  }

  /**
   * Check the content integrity and return the claims if valid
   *
   * @param content
   * @return
   * @throws ExpiredJwtException
   * @throws UnsupportedJwtException
   * @throws MalformedJwtException
   * @throws SignatureException
   * @throws IllegalArgumentException
   */
  public Claims checkAndParseJwt(String content) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
    return Jwts.parser()
        .setSigningKey(jwtSecretKey.getKey())
        .parseClaimsJws(content)
        .getBody();
  }

  /**
   * Check the xsrf token
   *
   * @param xsrfToken
   * @param jwtToken
   * @throws InvalidCookieException
   */
  public void checkXsrfToken(String xsrfToken, String jwtToken) throws InvalidCookieException {

    //Check token
    Object body = Jwts.parser()
        .setSigningKey(xsrfSecretKey.getKey())
        .parse(xsrfToken).getBody();

    if (!body.equals(hashJwtToken(jwtToken))) {
      throw new InvalidCookieException("Xsrf issue: mismatch");
    }
  }

  private String hashJwtToken(String jwtToken) {
    return Hashing.sha256().hashString(jwtToken, UTF_8).toString();
  }
}
