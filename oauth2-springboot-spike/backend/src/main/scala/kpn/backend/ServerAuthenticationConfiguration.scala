package kpn.backend

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
class ServerAuthenticationConfiguration(userService: UserService) extends WebSecurityConfigurerAdapter {

  override protected def configure(http: HttpSecurity): Unit = {
    http
      .addFilterAfter(new ServerAuthenticationFilter(), classOf[LogoutFilter])
      .csrf(csrf => csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse))
      .logout(logout =>
        logout
          .logoutUrl("/oauth2/logout")
          .logoutSuccessHandler((request, response, authentication) => {
            // the default is redirect to 'logoutSuccessUrl", but we just want http 200 OK status
          })
      )
      .oauth2Login(configurer =>
        configurer
          .successHandler(new ServerAuthenticationSuccessHandler())
          .userInfoEndpoint.userService(userService)
      )
  }
}
