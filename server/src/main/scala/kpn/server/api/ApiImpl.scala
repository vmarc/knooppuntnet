package kpn.server.api

import kpn.api.common.status.ActionTimestamp
import kpn.core.action.ApiAction
import kpn.core.util.Log
import kpn.server.repository.FrontendActionsRepository
import org.springframework.stereotype.Component

@Component
class ApiImpl(frontendActionsRepository: FrontendActionsRepository) extends Api {

  private val log = Log(classOf[Api])

  override def execute[T](user: Option[String], action: String, args: String)(f: => T): T = {
    val t1 = System.nanoTime()
    try {
      f
    } finally {
      val t2 = System.nanoTime()
      val elapsed: Long = (t2 - t1) / 1000
      log.info(s"$user $action($args) (${elapsed}ms)")

      frontendActionsRepository.saveApiAction(
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
