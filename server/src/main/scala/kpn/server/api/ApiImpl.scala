package kpn.server.api

import kpn.api.common.status.ActionTimestamp
import kpn.core.metrics.ApiAction
import kpn.core.util.Log
import kpn.server.config.RequestContext
import kpn.server.repository.MetricsRepository
import org.springframework.stereotype.Component

@Component
class ApiImpl(metricsRepository: MetricsRepository) extends Api {

  private val log = Log(classOf[Api])

  override def execute[T](action: String, args: String)(f: => T): T = {

    val t1 = System.nanoTime()
    try {
      f
    } finally {
      val t2 = System.nanoTime()
      val elapsed: Long = (t2 - t1) / 1000000

      RequestContext.instance.get() match {
        case None => log.info(s"NO LOGCONTEXT $action($args) (${elapsed}ms)")
        case Some(context) =>
          log.info(s"${context.deviceClass} ${context.user} $action($args) (${elapsed}ms)")
          metricsRepository.saveApiAction(
            ApiAction(
              context.remoteAddress,
              context.userAgentString,
              context.deviceClass,
              context.deviceName,
              context.user,
              action,
              args,
              ActionTimestamp.now(),
              elapsed
            )
          )
      }
    }
  }
}
