package kpn.server.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.context.SecurityContextPersistenceFilter
import org.springframework.stereotype.Component

@Component
class RestWebSecurityConfig(securityFilter: SecurityFilter) extends WebSecurityConfigurerAdapter {

  override protected def configure(http: HttpSecurity): Unit = {
    http.addFilterBefore(securityFilter, classOf[SecurityContextPersistenceFilter])
  }
}
