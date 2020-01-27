package kpn.server.api.authentication

import com.nimbusds.jose.JWSAlgorithm.HS256
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import org.apache.commons.codec.binary.Base64.decodeBase64
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.social.oauth1.OAuthToken
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(authenticationFacade: AuthenticationFacade, crypto: Crypto, cryptoKey: String) {

  private val userKey = "user" // TODO see: SecurityFilter
  private val accessTokenKey = "access-token" // TODO see: SecurityFilter
  private val SECRET = "from-server.properties-configuration-file"


  @GetMapping(value = Array("/json-api/login"))
  @ResponseBody def login(
    @RequestParam callbackUrl: String,
    @CookieValue(name = "knooppuntnet-user", required = false) user: String,
    response: HttpServletResponse
  ): ResponseEntity[_] = {
    val token = authenticationFacade.login(Option.apply(user), callbackUrl)
    val encryptedSecret = crypto.encrypt(token.getSecret)
    val cookie = new Cookie("knooppuntnet", encryptedSecret)
    cookie.setMaxAge(7 * 24 * 60 * 60)
    // cookie.setSecure(true)
    cookie.setHttpOnly(true)
    cookie.setPath("/")
    response.addCookie(cookie)
    new ResponseEntity[String](token.getValue, HttpStatus.OK)
  }

  @GetMapping(value = Array("/json-api/authenticated"))
  @ResponseBody def authenticated(
    @RequestParam oauth_token: String,
    @RequestParam oauth_verifier: String,
    @CookieValue(name = "knooppuntnet", required = false) oauth_token_secret: String,
    response: HttpServletResponse
  ): ResponseEntity[_] = {

    val secret = crypto.decrypt(oauth_token_secret)
    val token = new OAuthToken(oauth_token, secret)
    val user = authenticationFacade.authenticated(token, oauth_verifier)

    val encryptedAccessToken = crypto.encrypt(secret)
    val claimsSetBuilder = new JWTClaimsSet.Builder
    claimsSetBuilder.claim(userKey, user)
    claimsSetBuilder.claim(accessTokenKey, encryptedAccessToken)
    val decodedCryptoKey = decodeBase64(cryptoKey)
    val signer = new MACSigner(decodedCryptoKey)
    val signedJWT = new SignedJWT(new JWSHeader(HS256), claimsSetBuilder.build)
    signedJWT.sign(signer)
    val cookieContents = signedJWT.serialize

    val cookie = new Cookie("knooppuntnet", cookieContents)
    cookie.setMaxAge(52 * 7 * 24 * 60 * 60)
    // cookie.setSecure(true)
    cookie.setHttpOnly(true)
    cookie.setPath("/")

    response.addCookie(cookie)
    new ResponseEntity[String](user, HttpStatus.OK)
  }

  @GetMapping(value = Array("/json-api/logout"))
  @ResponseBody def logout(
    response: HttpServletResponse
  ): ResponseEntity[_] = {
    val cookie = new Cookie("knooppuntnet", "")
    cookie.setMaxAge(0) // a zero value here will delete the cookie
    // cookie.setSecure(true)
    cookie.setHttpOnly(true)
    cookie.setPath("/")
    response.addCookie(cookie)
    new ResponseEntity[String]("", HttpStatus.OK)
  }
}
