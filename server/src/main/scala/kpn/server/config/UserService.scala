package kpn.server.config

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

import java.nio.charset.Charset
import scala.xml.XML

@Component
class UserService extends OAuth2UserService[OAuth2UserRequest, OAuth2User] {

  override def loadUser(userRequest: OAuth2UserRequest): OAuth2User = {
    val accessToken = userRequest.getAccessToken.getTokenValue
    val userDetailsResponse = requestUserDetails(accessToken)
    userDetailsResponse.getStatusCode match {
      case HttpStatus.OK =>
        val user = parseUser(userDetailsResponse)
        new ServerAuthenticationUser(user)
      case _ =>
        throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED))
    }
  }

  private def requestUserDetails(accessToken: String): ResponseEntity[String] = {
    val url = "https://api.openstreetmap.org/api/0.6/user/details"
    val headers = new HttpHeaders()
    headers.setAccept(java.util.Arrays.asList(MediaType.TEXT_XML))
    headers.setAcceptCharset(java.util.Arrays.asList(Charset.forName("UTF-8")))
    headers.set(HttpHeaders.REFERER, "knooppuntnet.nl")
    headers.set(HttpHeaders.AUTHORIZATION, "knooppuntnet.nl")
    headers.setBearerAuth(accessToken)
    val entity = new HttpEntity[String]("", headers)
    val restTemplate = new RestTemplate()
    restTemplate.exchange(url, HttpMethod.GET, entity, classOf[String])
  }

  private def parseUser(response: ResponseEntity[String]): String = {
    val userXml = response.getBody
    new UserParser().parse(XML.loadString(userXml)).get
  }
}
