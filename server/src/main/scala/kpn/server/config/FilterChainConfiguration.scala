package kpn.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
class FilterChainConfiguration(
  serverAuthenticationFilter: ServerAuthenticationFilter,
  serverAuthenticationSuccessHandler: ServerAuthenticationSuccessHandler,
  userService: UserService,
  testEnabled: Boolean
) {

  @Bean
  def filterChain(http: HttpSecurity): SecurityFilterChain = {
    http
      .addFilterAfter(serverAuthenticationFilter, classOf[LogoutFilter])
      .addFilterAfter(new RequestContextFilter(testEnabled), classOf[ServerAuthenticationFilter])
      .authorizeRequests(authorizeRequests =>
        authorizeRequests.anyRequest().permitAll()
      )
      //.csrf(csrf => csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse))
      .csrf(csrf => csrf.disable)
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
    http.build()
  }
}
