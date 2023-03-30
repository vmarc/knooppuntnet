package kpn.server.api

import kpn.api.common.status.ActionTimestamp
import kpn.core.metrics.ApiAction
import kpn.core.util.Log
import kpn.server.repository.MetricsRepository
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest

@Component
class ApiImpl(metricsRepository: MetricsRepository) extends Api {

  private val log = Log(classOf[Api])
  private val userAgentAnalyzer = UserAgentAnalyzer.newBuilder
    .withCache(10000)
    .hideMatcherLoadStats
    .withField("DeviceClass")
    .withField("DeviceName")
    .build


  override def execute[T](request: HttpServletRequest, user: Option[String], action: String, args: String)(f: => T): T = {


    val t1 = System.nanoTime()
    try {
      f
    } finally {
      val t2 = System.nanoTime()
      val elapsed: Long = (t2 - t1) / 1000000

      val headers: java.util.Map[String, String] = new java.util.HashMap[String, String]();
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

      log.info(s"$user $action($args) (${elapsed}ms)")

      metricsRepository.saveApiAction(
        ApiAction(
          remoteAddress,
          userAgentString,
          deviceClass,
          deviceName,
          user,
          action,
          args,
          ActionTimestamp.now(),
          elapsed
        )
      )
    }
  }
}
