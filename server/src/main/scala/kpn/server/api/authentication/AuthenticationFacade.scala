package kpn.server.api.authentication

import org.springframework.social.oauth1.OAuthToken

trait AuthenticationFacade {

  def login(callbackUrl: String): OAuthToken

  def authenticated(token: OAuthToken, verifier: String): String

}
