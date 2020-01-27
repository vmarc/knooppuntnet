package kpn.server.config

import java.text.ParseException
import java.util

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import kpn.server.api.authentication.Crypto
import org.apache.commons.codec.binary.Base64.decodeBase64
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean


@Component
class SecurityFilter(crypto: Crypto, cryptoKey: String) extends GenericFilterBean {

  private val userKey = "user"
  private val accessTokenKey = "access-token"

  override def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain): Unit = {
    val httpRequest = servletRequest.asInstanceOf[HttpServletRequest]
    val cookies = httpRequest.getCookies
    if (cookies != null) {
      cookies.find(c => "knooppuntnet" == c.getName) match {
        case None =>
        case Some(cookie) =>
          extractAuthentication(cookie.getValue)
      }
    }
    filterChain.doFilter(httpRequest, servletResponse)
  }

  private def extractAuthentication(bearer: String): Unit = {
    try {
      val signedJWT: SignedJWT = SignedJWT.parse(bearer)
      val decodedCryptoKey = decodeBase64(cryptoKey)

      val signatureVerification: Boolean = signedJWT.verify(new MACVerifier(decodedCryptoKey))
      if (signatureVerification) {
        val claims: util.Map[String, AnyRef] = signedJWT.getJWTClaimsSet.getClaims
        val encryptedAccessToken: Any = claims.get(accessTokenKey)
        if (encryptedAccessToken != null) {
          val accessToken: String = crypto.decrypt(encryptedAccessToken.toString)
          // At this moment we do not use the access token.
          // We could use it in the future to make a request to OSM to verify
          // if access is still granted to us. Perhaps do not do this all the
          // time but look at 'issueTime' (currently not added in the claims)
          // to decide whether or not to do the check (once per calendar day?
          // once per hour?).
          val userValue: Any = claims.get(userKey)
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
        System.out.println("ParseException" + e.getMessage)
      case e: JOSEException =>
        System.out.println("JOSEException " + e.getMessage)
    }
  }

}
