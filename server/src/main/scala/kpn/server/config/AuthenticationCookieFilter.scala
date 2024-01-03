package kpn.server.config

import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletResponse
import kpn.core.util.Log
import org.apache.commons.codec.binary.Base64.decodeBase64
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component

/*
  Extracts the username from the {@code knooppuntnet} cookie, and stores this in the {@code SecurityContext}.
 */
@Component
class AuthenticationCookieFilter(cryptoKey: String) extends HttpFilter {

  private val log = Log(classOf[AuthenticationCookieFilter])

  private val verifier = new MACVerifier(decodeBase64(cryptoKey))

  override def doFilter(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ): Unit = {
    val cookies = request.getCookies
    if (cookies != null) {
      cookies.find(cookie => AuthenticationConfiguration.cookieName == cookie.getName) match {
        case None =>
        case Some(cookie) =>
          extractUsername(cookie) match {
            case None =>
            case Some(user) =>
              val authentication = new PreAuthenticatedAuthenticationToken(user, null)
              authentication.setAuthenticated(true)
              SecurityContextHolder.getContext.setAuthentication(authentication)
              authentication
          }
      }
    }
    filterChain.doFilter(request, response)
  }

  private def extractUsername(cookie: Cookie): Option[String] = {
    try {
      val signedJWT = SignedJWT.parse(cookie.getValue)
      try {
        val signatureVerification = signedJWT.verify(verifier)
        if (signatureVerification) {
          val claims = signedJWT.getJWTClaimsSet.getClaims
          val userValue = claims.get(AuthenticationConfiguration.userKey)
          if (userValue != null) {
            Some(userValue.toString)
          }
          else {
            None
          }
        }
        else {
          None
        }
      }
      catch {
        case e: Exception =>
          log.warn(s"Could not verify signature in knooppuntnet cookie: ${e.getMessage}")
          None
      }
    }
    catch {
      case e: Exception =>
        log.warn(s"Could not parse knooppuntnet cookie: ${e.getMessage}")
        None
    }
  }
}
