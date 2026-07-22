package kpn.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter

@Configuration
@Profile(Array("dev"))
class FilterChainConfigurationDev(
  testEnabled: Boolean
) {

  @Bean
  def filterChain(http: HttpSecurity): SecurityFilterChain = {
    http
      .addFilterAfter(new RequestContextFilter(testEnabled), classOf[LogoutFilter])
      .authorizeHttpRequests(authorizeRequests =>
        authorizeRequests.anyRequest().permitAll()
      )
      //.csrf(csrf => csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse))
      .csrf(csrf => csrf.disable)
    http.build()
  }
}
