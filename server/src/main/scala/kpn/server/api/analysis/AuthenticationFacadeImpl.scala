package kpn.server.api.analysis

import kpn.core.util.Log
import org.springframework.social.oauth1.AuthorizedRequestToken
import org.springframework.social.oauth1.GenericOAuth1ServiceProvider
import org.springframework.social.oauth1.OAuth1Template
import org.springframework.social.oauth1.OAuth1Version
import org.springframework.social.oauth1.OAuthToken
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

import scala.xml.XML

@Component
class AuthenticationFacadeImpl(
  oauthApplicationKey: String,
  oauthApplicationSecret: String
) extends AuthenticationFacade {

  private val log = Log(classOf[AuthenticationFacadeImpl])

  private val oauthTemplate = new OAuth1Template(
    oauthApplicationKey,
    oauthApplicationSecret,
    "https://www.openstreetmap.org/oauth/request_token",
    "https://www.openstreetmap.org/oauth/authorize",
    "https://www.openstreetmap.org/oauth/access_token"
  )

  def login(user: Option[String], callbackUrl: String): OAuthToken = {
    log.elapsed {
      val token = oauthTemplate.fetchRequestToken(callbackUrl, null)
      (s"fetched requestToken", token)
    }
  }

  def authenticated(token: OAuthToken, verifier: String): String = {

    val requestToken = new AuthorizedRequestToken(token, verifier)

    val accessToken = oauthTemplate.exchangeForAccessToken(requestToken, null)

    val provider = new GenericOAuth1ServiceProvider(
      oauthApplicationKey,
      oauthApplicationSecret,
      "https://www.openstreetmap.org/oauth/request_token",
      "https://www.openstreetmap.org/oauth/authorize",
      "https://www.openstreetmap.org/oauth/access_token",
      "???",
      OAuth1Version.CORE_10_REVISION_A
    )

    val op: RestOperations = provider.getApi(accessToken.getValue, accessToken.getSecret)
    val userXml = op.getForObject("https://api.openstreetmap.org/api/0.6/user/details", classOf[String])
    new UserParser().parse(XML.loadString(userXml)).get
  }

}
