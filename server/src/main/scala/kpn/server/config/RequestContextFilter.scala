package kpn.server.config

import kpn.server.api.CurrentUser
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class RequestContextFilter extends GenericFilterBean {

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

    RequestContext.instance.set(
      Some(
        RequestContext(
          remoteAddress,
          userAgentString,
          deviceClass,
          deviceName,
          CurrentUser.name
        )
      )
    )
    filterChain.doFilter(httpRequest, servletResponse)
    RequestContext.instance.remove()
  }
}
