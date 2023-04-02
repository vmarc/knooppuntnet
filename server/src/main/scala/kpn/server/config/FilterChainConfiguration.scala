package kpn.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
class FilterChainConfiguration(
  securityFilter: SecurityFilter
) {
  private val requestContextFilter = new RequestContextFilter()

  @Bean
  def filterChain(http: HttpSecurity): SecurityFilterChain = {
    http.addFilterAfter(securityFilter, classOf[BasicAuthenticationFilter])
    http.addFilterAfter(requestContextFilter, classOf[SecurityFilter])
    // .authorizeRequests
    // .antMatchers("/**").permitAll()
    // .antMatchers("/**/changes").fullyAuthenticated()
    http.csrf.disable
    http.build()
  }
}
