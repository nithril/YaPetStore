package io.github.nithril.yapetstore.security;

import static java.util.Arrays.asList;

import io.github.nithril.yapetstore.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import javax.servlet.Filter;

/**
 * Created by nlabrot on 21/11/16.
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  public static final String ROLE_USER = "ROLE_USER";

  @Value("${jwt.cookie.name}")
  private String jwtCookieName;

  @Value("${xsrf.header.name}")
  private String xsrfHeaderName;

  @Autowired
  private SecurityService securityService;


  private final SecureRandom secureRandom;

  public SecurityConfiguration() throws NoSuchAlgorithmException {
    secureRandom = SecureRandom.getInstanceStrong();
  }

  @Bean
  public FilterChainProxy configure() throws Exception {
    return new FilterChainProxy(asList(
        // auth controller is not protected
        new DefaultSecurityFilterChain(new AntPathRequestMatcher("/api/auth/**")),
        // api are protected
        new DefaultSecurityFilterChain(new AntPathRequestMatcher("/api/**"), jwtFilter()),
        new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**/*")),
        new DefaultSecurityFilterChain(new AntPathRequestMatcher("/"))
    ));

  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(Arrays.asList(authenticationProvider()));
  }


  @Bean
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(
        asList(
            new User("user", "user123", Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER))),
            new User("admin", "admin123", Collections.singletonList(new SimpleGrantedAuthority(ROLE_ADMIN)))
        )
    );
  }

  public Filter jwtFilter() {
    return new JwtFilter(jwtCookieName, xsrfHeaderName, userDetailsService(), securityService);
  }

  @Bean
  public SecretKey jwtSecretKey() {
    return new SecretKey(generateKey());
  }

  @Bean
  public SecretKey xsrfSecretKey() {
    return new SecretKey(generateKey());
  }

  private byte[] generateKey() {
    byte[] key = new byte[32];
    secureRandom.nextBytes(key);
    return key;
  }
}
