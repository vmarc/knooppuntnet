package kpn.backend

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class UserAuthentication(user: String) extends AbstractAuthenticationToken(new java.util.ArrayList[GrantedAuthority]()) {

  override def getCredentials: Object = null

  override def getPrincipal: Object = user

}
