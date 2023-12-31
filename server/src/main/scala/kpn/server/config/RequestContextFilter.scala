package kpn.server.config

import kpn.server.api.CurrentUser
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.web.filter.GenericFilterBean

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest

class RequestContextFilter(testEnabled: Boolean) extends GenericFilterBean {

  private val userAgentAnalyzer = UserAgentAnalyzer.newBuilder
    .withCache(10000)
    //.hideMatcherLoadStats
    .withField("DeviceClass")
    .withField("DeviceName")
    .build

  override def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain): Unit = {
    val httpRequest = servletRequest.asInstanceOf[HttpServletRequest]
    val headers: java.util.Map[String, String] = new java.util.HashMap[String, String]();
    val headerIterator = httpRequest.getHeaderNames
    while (headerIterator.hasMoreElements) {
      val headerName = headerIterator.nextElement()
      val headerValue = httpRequest.getHeader(headerName)
      headers.put(headerName, headerValue)
    }
    val result = userAgentAnalyzer.parse(headers)
    val remoteAddress = httpRequest.getRemoteAddr
    val userAgentString = Option(result.getUserAgentString)
    val deviceClass = Option(result.get("DeviceClass").getValue)
    val deviceName = Option(result.get("DeviceName").getValue)
    val user = if (testEnabled) Some("test-user") else CurrentUser.name
    RequestContext.instance.set(
      Some(
        RequestContext(
          remoteAddress,
          userAgentString,
          deviceClass,
          deviceName,
          user
        )
      )
    )
    filterChain.doFilter(httpRequest, servletResponse)
    RequestContext.instance.remove()
  }
}
