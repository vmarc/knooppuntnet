package kpn.server.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component

@Component
class CustomAuthorizationRequestResolver(
  clientRegistrationRepository: ClientRegistrationRepository
) extends OAuth2AuthorizationRequestResolver {

  private val defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
    clientRegistrationRepository,
    "/api/oauth2/authorization"
  )

  override def resolve(request: HttpServletRequest): OAuth2AuthorizationRequest = {
    val defaultAuth2AuthorizationRequest = defaultResolver.resolve(request)
    replaceRedirectUri(request, defaultAuth2AuthorizationRequest)
  }

  override def resolve(request: HttpServletRequest, clientRegistrationId: String): OAuth2AuthorizationRequest = {
    val defaultAuth2AuthorizationRequest = defaultResolver.resolve(request, clientRegistrationId)
    replaceRedirectUri(request, defaultAuth2AuthorizationRequest)
  }

  private def replaceRedirectUri(
    request: HttpServletRequest,
    defaultAuth2AuthorizationRequest: OAuth2AuthorizationRequest
  ): OAuth2AuthorizationRequest = {

    if (defaultAuth2AuthorizationRequest == null) {
      null
    }
    else {
      val redirectUri = composeRedirectUri(request)
      val b = OAuth2AuthorizationRequest.from(defaultAuth2AuthorizationRequest)
      b.redirectUri(redirectUri)
      b.build()
    }
  }

  private def composeRedirectUri(request: HttpServletRequest): String = {

    val forwardedProto = request.getHeader("X-Forwarded-Proto")
    val forwardedHost = request.getHeader("X-Forwarded-Host")

    val sb = new StringBuilder()
    if (forwardedProto != null && forwardedHost != null) {
      sb.append(forwardedProto)
      sb.append("://")
      sb.append(forwardedHost)
    }
    else {
      sb.append(request.getScheme)
      sb.append("://")
      sb.append(request.getServerName)
      sb.append(":")
      sb.append(request.getServerPort)
    }
    sb.append("/api/oauth2/code/osm")
    sb.toString()
  }
}
