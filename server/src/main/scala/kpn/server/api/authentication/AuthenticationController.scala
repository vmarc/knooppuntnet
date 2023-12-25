package kpn.server.api.authentication

import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import kpn.server.api.Api
import org.apache.commons.codec.binary.Base64.decodeBase64
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

import java.nio.charset.Charset
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import scala.xml.XML

@RestController
class AuthenticationController(api: Api, oauthClientId: String, crypto: Crypto, cryptoKey: String) {

  private val log = LoggerFactory.getLogger(classOf[AuthenticationController])

  @GetMapping(value = Array("/api/client-id"))
  def clientId(): String = {
    oauthClientId
  }

  @GetMapping(value = Array("/api/authenticated"))
  @ResponseBody def authenticated(
    @RequestParam accessToken: String,
    response: HttpServletResponse
  ): ResponseEntity[_] = {
    api.execute("authenticated") {
      val userDetailsResponse = requestUserDetails(accessToken)
      userDetailsResponse.getStatusCode match {
        case HttpStatus.OK =>
          val user = parseUser(userDetailsResponse)
          log.info(s"GET authenticated() user $user")
          response.addCookie(buildCookie(accessToken, user))
          new ResponseEntity[String](user, HttpStatus.OK)

        case _ =>
          log.error("GET authenticated() UNAUTHORIZED Could not retrieve user xml from OSM API")
          new ResponseEntity[String]("", HttpStatus.UNAUTHORIZED)
      }
    }
  }

  @GetMapping(value = Array("/api/logout"))
  @ResponseBody def logout(response: HttpServletResponse): ResponseEntity[_] = {
    api.execute("logout") {
      response.addCookie(createCookie("", 0))
      new ResponseEntity[String]("", HttpStatus.OK)
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

  private def buildCookie(accessToken: String, user: String): Cookie = {
    val encryptedAccessToken = crypto.encrypt(accessToken)
    val claimsSetBuilder = new JWTClaimsSet.Builder
    claimsSetBuilder.claim(AuthenticationConfiguration.userKey, user)
    claimsSetBuilder.claim(AuthenticationConfiguration.accessTokenKey, encryptedAccessToken)
    val decodedCryptoKey = decodeBase64(cryptoKey)
    val signer = new MACSigner(decodedCryptoKey)
    val signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSetBuilder.build)
    signedJWT.sign(signer)
    val cookieContents = signedJWT.serialize
    createCookie(cookieContents, 52 * 7 * 24 * 60 * 60)
  }

  private def createCookie(value: String, maxAge: Int): Cookie = {
    val cookie = new Cookie(AuthenticationConfiguration.cookieName, value)
    cookie.setMaxAge(maxAge) // a zero value here will delete the cookie
    // cookie.setSecure(true)
    cookie.setHttpOnly(true)
    cookie.setPath("/")
    cookie
  }
}
