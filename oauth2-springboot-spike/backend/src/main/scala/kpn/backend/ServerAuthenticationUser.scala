package kpn.backend

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority

class ServerAuthenticationUser(name: String) extends OAuth2User {
  private val authorities = new java.util.ArrayList[OAuth2UserAuthority]()
  private val attributes = new java.util.HashMap[String, AnyRef]()
  attributes.put("name", name)

  override def getName: String = {
    name
  }

  override def getAttributes: java.util.Map[String, AnyRef] = {
    attributes
  }

  override def getAuthorities: java.util.Collection[_ <: GrantedAuthority] = {
    authorities
  }
}
