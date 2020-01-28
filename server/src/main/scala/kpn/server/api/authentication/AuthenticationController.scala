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

  @GetMapping(value = Array("/json-api/login"))
  @ResponseBody def login(@RequestParam callbackUrl: String, response: HttpServletResponse): ResponseEntity[_] = {
    val token = authenticationFacade.login(callbackUrl)
    val encryptedSecret = crypto.encrypt(token.getSecret)
    response.addCookie(createCookie(encryptedSecret, 60))
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
    claimsSetBuilder.claim(AuthenticationConfiguration.userKey, user)
    claimsSetBuilder.claim(AuthenticationConfiguration.accessTokenKey, encryptedAccessToken)
    val decodedCryptoKey = decodeBase64(cryptoKey)
    val signer = new MACSigner(decodedCryptoKey)
    val signedJWT = new SignedJWT(new JWSHeader(HS256), claimsSetBuilder.build)
    signedJWT.sign(signer)
    val cookieContents = signedJWT.serialize

    response.addCookie(createCookie(cookieContents, 52 * 7 * 24 * 60 * 60))
    new ResponseEntity[String](user, HttpStatus.OK)
  }

  @GetMapping(value = Array("/json-api/logout"))
  @ResponseBody def logout(response: HttpServletResponse): ResponseEntity[_] = {
    response.addCookie(createCookie("", 0))
    new ResponseEntity[String]("", HttpStatus.OK)
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
