package kpn.server.api

import java.net.URLEncoder

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import kpn.server.api.analysis.AuthenticationFacade
import kpn.server.api.analysis.Crypto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.social.oauth1.OAuthToken
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(authenticationFacade: AuthenticationFacade, crypto: Crypto) {

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

    val cookie = new Cookie("knooppuntnet-user", URLEncoder.encode(user, "UTF-8"))
    cookie.setMaxAge(7 * 24 * 60 * 60)
    //    cookie.setSecure(true)
    cookie.setHttpOnly(true)
    cookie.setPath("/")
    response.addCookie(cookie)
    new ResponseEntity[String]("", HttpStatus.OK)
  }

}
