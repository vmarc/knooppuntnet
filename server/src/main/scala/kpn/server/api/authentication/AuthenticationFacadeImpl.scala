package kpn.server.api.authentication

import java.nio.charset.Charset

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoParser
import org.apache.commons.io.FileUtils
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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

  override def login(callbackUrl: String): OAuthToken = {
    log.elapsed {
      val token = oauthTemplate.fetchRequestToken(callbackUrl, null)
      (s"fetched requestToken", token)
    }
  }

  override def authenticated(token: OAuthToken, verifier: String): String = {

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

    val url = "https://api.openstreetmap.org/api/0.6/user/details"
    val headers = new HttpHeaders()
    headers.setAccept(java.util.Arrays.asList(MediaType.TEXT_XML))
    headers.setAcceptCharset(java.util.Arrays.asList(Charset.forName("UTF-8")))
    headers.set(HttpHeaders.REFERER, "knooppuntnet.nl")
    val entity = new HttpEntity[String]("", headers)

    try {
      val response: ResponseEntity[String] = op.exchange(url, HttpMethod.GET, entity, classOf[String])
      response.getStatusCode match {
        case HttpStatus.OK =>
          val userXml = response.getBody
          log.info(userXml)
          new UserParser().parse(XML.loadString(userXml)).get
        case _ =>
          log.error(s"Could not retrieve user xml from OSM API")
          ""
      }
    }
  }
}
