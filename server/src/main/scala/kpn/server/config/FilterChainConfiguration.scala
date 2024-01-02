package kpn.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter

@Configuration
class FilterChainConfiguration(
  serverAuthenticationFilter: ServerAuthenticationFilter,
  loginSuccessHandler: ServerAuthenticationLoginSuccessHandler,
  logoutSuccessHandler: ServerAuthenticationLogoutSuccessHandler,
  userService: UserService,
  testEnabled: Boolean
) {

  @Bean
  def filterChain(http: HttpSecurity): SecurityFilterChain = {
    http
      .addFilterAfter(serverAuthenticationFilter, classOf[LogoutFilter])
      .addFilterAfter(new RequestContextFilter(testEnabled), classOf[ServerAuthenticationFilter])
      .authorizeHttpRequests(authorizeRequests =>
        authorizeRequests.anyRequest().permitAll()
      )
      //.csrf(csrf => csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse))
      .csrf(csrf => csrf.disable)
      .logout(logout =>
        logout
          .logoutUrl("/oauth2/logout")
          .logoutSuccessHandler(logoutSuccessHandler)
      )
      .oauth2Login(configurer =>
        configurer
          .successHandler(loginSuccessHandler)
          .userInfoEndpoint(userInfoEndpoint =>
            userInfoEndpoint.userService(userService)
          )
      )

    http.build()
  }
}
