package kpn.server.config

import kpn.server.api.CurrentUser
import nl.basjes.parse.useragent.UserAgentAnalyzer
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class RequestContextFilter(testEnabled: Boolean) extends HttpFilter {

  private val userAgentAnalyzer = UserAgentAnalyzer.newBuilder
    .withCache(10000)
    //.hideMatcherLoadStats
    .withField("DeviceClass")
    .withField("DeviceName")
    .build

  override def doFilter(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ): Unit = {

    val headers = new java.util.HashMap[String, String]();
    val headerIterator = request.getHeaderNames
    while (headerIterator.hasMoreElements) {
      val headerName = headerIterator.nextElement()
      val headerValue = request.getHeader(headerName)
      headers.put(headerName, headerValue)
    }
    val result = userAgentAnalyzer.parse(headers)
    val remoteAddress = request.getRemoteAddr
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
    filterChain.doFilter(request, response)
    RequestContext.instance.remove()
  }
}
