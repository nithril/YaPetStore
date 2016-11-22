package io.github.nithril.yapetstore.security;


import io.github.nithril.yapetstore.service.SecurityService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.web.filter.GenericFilterBean;

import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nlabrot on 21/11/16.
 */
@Slf4j
public class JwtFilter extends GenericFilterBean {

  private final String jwtCookieName;
  private final SecretKey jwtSecretKey;

  private final String xsrfHeaderName;
  private final SecretKey xsrfSecretKey;

  private final UserDetailsService userDetailsService;


  private final SecurityService securityService;


  public JwtFilter(String jwtCookieName, SecretKey jwtSecretKey, String xsrfHeaderName, SecretKey xsrfSecretKey, UserDetailsService userDetailsService, SecurityService securityService) {
    this.jwtCookieName = jwtCookieName;
    this.jwtSecretKey = jwtSecretKey;
    this.xsrfHeaderName = xsrfHeaderName;
    this.xsrfSecretKey = xsrfSecretKey;
    this.userDetailsService = userDetailsService;
    this.securityService = securityService;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    try {

      Cookie[] cookies = request.getCookies();
      if (cookies == null) {
        response.setStatus(401);
        return;
      }

      // Get cookie
      Cookie jwtCookie = Arrays.stream(cookies)
          .filter(cookie -> jwtCookieName.equals(cookie.getName()))
          .findFirst().orElse(null);

      if (jwtCookie == null) {
        response.setStatus(401);
        return;
      }

      // Check jwt token
      Claims claims = Jwts.parser()
          .setSigningKey(jwtSecretKey.getValue())
          .parseClaimsJws(jwtCookie.getValue())
          .getBody();

      // Check xsrf token
      checkXsrf(request, jwtCookie.getValue());

      // All is correct, retrieve the username
      UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());

      // And set the security context
      SecurityContextHolder.getContext()
          .setAuthentication(new UsernamePasswordAuthenticationToken(user, claims, user.getAuthorities()));

      chain.doFilter(req, res);

    } catch (Exception exception) {
      LOG.error(exception.getMessage(), exception);
      response.setStatus(401);
    } finally {
      // Once all filter are applied, reset the security context
      SecurityContextHolder.clearContext();
    }
  }


  private void checkXsrf(HttpServletRequest request, String jwtToken) {
    String xsrfToken = request.getHeader(xsrfHeaderName);

    if (xsrfToken == null) {
      throw new InvalidCookieException("Xsrf issue: no header");
    }

    securityService.checkXsrfToken(xsrfToken, jwtToken);
  }
}
