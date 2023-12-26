package kpn.backend

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
class AuthenticationConfiguration(userService: UserService) extends WebSecurityConfigurerAdapter {
  override protected def configure(http: HttpSecurity): Unit = {
    http.authorizeRequests(authorizeRequests =>
        authorizeRequests.antMatchers("/", "/error", "/api/page1", "/api/page2")
          .permitAll
          .anyRequest
          .authenticated
      )
      .exceptionHandling(exceptionHandling =>
        exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
      )
      .oauth2Login(e => e.userInfoEndpoint.userService(userService))
    /*
      .failureHandler((request, response, exception) -> {
            request.getSession().setAttribute("error.message", exception.getMessage());
            handler.onAuthenticationFailure(request, response, exception);
              })
     */
  }
}
