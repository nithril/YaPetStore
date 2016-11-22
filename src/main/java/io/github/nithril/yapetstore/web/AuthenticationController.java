package io.github.nithril.yapetstore.web;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonMap;


import com.google.common.hash.Hashing;
import io.github.nithril.yapetstore.domain.User;
import io.github.nithril.yapetstore.security.SecretKey;
import io.github.nithril.yapetstore.service.SecurityService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nlabrot on 21/11/16.
 */
@RestController
@RequestMapping(value = "/api/auth")
@Slf4j
public class AuthenticationController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private SecurityService securityService;

  @Value("${jwt.cookie.name}")
  private String jwtCookieName;

  @Value("${xsrf.cookie.name}")
  private String xsrfCookieName;

  @Value("${jwt.cookie.maxage}")
  private int cookieMaxAge;


  @PostMapping(value = "/login")
  public ResponseEntity login(@RequestBody User user, HttpServletResponse response) {

    try {
      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

      authenticationManager.authenticate(token);

      String jwtToken = createAndSetJwtCookie(user, response);

      createAndSetXsrfCookie(response, jwtToken);

      response.setStatus(200);
      return null;
    } catch (AuthenticationException e) {
      return ResponseEntity.badRequest().body(singletonMap("message", e.getMessage()));
    }
  }

  @GetMapping(value = "/logout")
  public void logout(HttpServletResponse response) {
    LOG.info("Request logout");
    // Reset jwt cookie
    Cookie jwtCookie = new Cookie(jwtCookieName, "");
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(0);
    response.addCookie(jwtCookie);

    // Reset xsrf cookie
    Cookie xsrfCookie = new Cookie(xsrfCookieName, "");
    xsrfCookie.setPath("/");
    xsrfCookie.setMaxAge(0);
    response.addCookie(xsrfCookie);

    response.setStatus(200);
  }


  private void createAndSetXsrfCookie(HttpServletResponse response, String jwtToken) {
    String xsrfToken = securityService.createXsrfToken(jwtToken);

    Cookie xsrfCookie = new Cookie(xsrfCookieName, xsrfToken);
    xsrfCookie.setMaxAge(cookieMaxAge);
    xsrfCookie.setHttpOnly(false);
    xsrfCookie.setPath("/");


    response.addCookie(xsrfCookie);
  }

  private String createAndSetJwtCookie(@RequestBody User user, HttpServletResponse response) {
    String jwtToken = securityService.createJwtToken(user);

    Cookie jwtCookie = new Cookie(jwtCookieName, jwtToken);
    jwtCookie.setMaxAge(cookieMaxAge);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setPath("/");

    response.addCookie(jwtCookie);
    return jwtToken;
  }
}
