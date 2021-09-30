package kpn.server.api

import kpn.api.common.status.ActionTimestamp
import kpn.core.metrics.ApiAction
import kpn.core.util.Log
import kpn.server.repository.MetricsRepository
import org.springframework.stereotype.Component

@Component
class ApiImpl(metricsRepository: MetricsRepository) extends Api {

  private val log = Log(classOf[Api])

  override def execute[T](user: Option[String], action: String, args: String)(f: => T): T = {
    val t1 = System.nanoTime()
    try {
      f
    } finally {
      val t2 = System.nanoTime()
      val elapsed: Long = (t2 - t1) / 1000000
      log.info(s"$user $action($args) (${elapsed}ms)")

      metricsRepository.saveApiAction(
        ApiAction(
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
