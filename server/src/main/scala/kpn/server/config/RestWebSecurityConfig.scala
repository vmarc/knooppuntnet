package kpn.server.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component

@Component
class RestWebSecurityConfig(securityFilter: SecurityFilter) extends WebSecurityConfigurerAdapter(true) {

  override protected def configure(http: HttpSecurity): Unit = {
    http.addFilterAfter(securityFilter, classOf[BasicAuthenticationFilter])
      .csrf.disable
  }
}
