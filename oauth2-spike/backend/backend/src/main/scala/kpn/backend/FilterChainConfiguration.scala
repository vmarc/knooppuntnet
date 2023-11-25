package kpn.backend

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain

@Configuration
class FilterChainConfiguration(  securityFilter: SecurityFilter) {

  @Bean
  def filterChain(http: HttpSecurity): SecurityFilterChain = {
    http.addFilterAfter(securityFilter, classOf[BasicAuthenticationFilter])
    // .authorizeRequests
    // .antMatchers("/**").permitAll()
    // .antMatchers("/**/changes").fullyAuthenticated()
    http.csrf.disable // TODO deprecated, can remove?
    http.build()
  }
}
