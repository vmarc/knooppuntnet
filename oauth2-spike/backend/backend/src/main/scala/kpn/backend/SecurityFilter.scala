package kpn.backend

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.codec.binary.Base64.decodeBase64
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

import java.text.ParseException
import java.util

@Component
class SecurityFilter(crypto: Crypto, cryptoKey: String) extends GenericFilterBean {

  private val verifier = new MACVerifier(decodeBase64(cryptoKey))

  override def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain): Unit = {
    val httpRequest = servletRequest.asInstanceOf[HttpServletRequest]
    val cookies = httpRequest.getCookies
    if (cookies != null) {
      cookies.find(cookie => AuthenticationConfiguration.cookieName == cookie.getName) match {
        case Some(cookie) => authenticate(cookie.getValue)
        case None =>
          SecurityContextHolder.getContext.setAuthentication(null)
      }
    }
    filterChain.doFilter(httpRequest, servletResponse)
  }

  private def authenticate(tokenString: String): Unit = {
    try {
      val signedJWT: SignedJWT = SignedJWT.parse(tokenString)
      val signatureVerification: Boolean = signedJWT.verify(verifier)
      if (signatureVerification) {
        val claims: util.Map[String, AnyRef] = signedJWT.getJWTClaimsSet.getClaims
        val encryptedAccessToken: Any = claims.get(AuthenticationConfiguration.accessTokenKey)
        if (encryptedAccessToken != null) {
          // At this moment we do not use the access token.
          // We could use it in the future to make a request to OSM to verify
          // if access is still granted to us. Perhaps do not do this all the
          // time but look at 'issueTime' (currently not added in the claims)
          // to decide whether or not to do the check (once per calendar day?
          // once per hour?).

          // val accessToken: String = crypto.decrypt(encryptedAccessToken.toString)

          val userValue: Any = claims.get(AuthenticationConfiguration.userKey)
          if (userValue != null) {
            val user: String = userValue.toString
            val authentication = new UserAuthentication(user)
            authentication.setAuthenticated(true)
            SecurityContextHolder.getContext.setAuthentication(authentication)
          }
        }
      }
    } catch {
      case e: ParseException =>
        System.out.println("ParseException" + e.getMessage) // TODO use logging instead?
      case e: JOSEException =>
        System.out.println("JOSEException " + e.getMessage) // TODO use logging instead?
    }
  }
}
