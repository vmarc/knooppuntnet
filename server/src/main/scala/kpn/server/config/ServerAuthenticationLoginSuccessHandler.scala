package kpn.server.config

import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.JWSAlgorithm.HS256
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import jakarta.servlet.http.Cookie
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.codec.binary.Base64.decodeBase64

@Component
class ServerAuthenticationLoginSuccessHandler(cryptoKey: String) extends SimpleUrlAuthenticationSuccessHandler {

  override def onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ): Unit = {
    val session = request.getSession(false)
    if (session != null) {
      val returnUrl = session.getAttribute(AuthenticationConfiguration.returnUrlKey).toString
      if (returnUrl != null) {
        session.removeAttribute(AuthenticationConfiguration.returnUrlKey)
        session.invalidate()
        response.addCookie(knooppuntnetCookie(authentication))
        response.addCookie(sessionResetCookie())
        response.sendRedirect(returnUrl)
      }
    }
    super.onAuthenticationSuccess(request, response, authentication)
  }

  private def knooppuntnetCookie(authentication: Authentication): Cookie = {
    val oneYear = 52 * 7 * 24 * 60 * 60
    val value = knooppuntnetCookieValue(authentication.getName)
    val cookie = new Cookie(AuthenticationConfiguration.cookieName, value)
    cookie.setMaxAge(oneYear)
    cookie.setSecure(false) // allow non-https requests in development
    cookie.setHttpOnly(true)
    cookie.setPath("/")
    cookie
  }

  private def knooppuntnetCookieValue(user: String): String = {
    val claimsSetBuilder = new JWTClaimsSet.Builder
    claimsSetBuilder.claim(AuthenticationConfiguration.userKey, user)
    val decodedCryptoKey = decodeBase64(cryptoKey)
    val signer = new MACSigner(decodedCryptoKey)
    val signedJWT = new SignedJWT(new JWSHeader(HS256), claimsSetBuilder.build)
    signedJWT.sign(signer)
    signedJWT.serialize
  }

  private def sessionResetCookie(): Cookie = {
    val sessionCookie = new Cookie("JSESSIONID", null)
    sessionCookie.setMaxAge(0) // a zero value here will delete the cookie
    sessionCookie.setPath("/")
    sessionCookie
  }
}
