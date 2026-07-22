package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorMessage
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.core.util.ValidationException
import kpn.server.json.Json
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorRouteUpdateExecutor(
  monitorAdd: MonitorAdd,
  monitorUpdate: MonitorUpdate,
  monitorGpxUpload: MonitorGpxUpload,
  monitorGpxDelete: MonitorGpxDelete,
) {

  private val log = Log(classOf[MonitorRouteUpdateExecutor])

  private val actionStrategies: Map[MonitorAction, MonitorUpdateArgs => Unit] = Map(
    MonitorAction.add -> monitorAdd.execute,
    MonitorAction.update -> monitorUpdate.execute,
    MonitorAction.gpxUpload -> monitorGpxUpload.execute,
    MonitorAction.gpxDelete -> monitorGpxDelete.execute
  )

  def execute(args: MonitorUpdateArgs): Unit = {
    try {
      val action = args.update.action
      actionStrategies.get(action) match {
        case Some(strategy) => strategy(args)
        case None => throw new IllegalArgumentException(s"Unknown action type: $action")
      }
    }
    catch {
      case e: RuntimeException =>
        handleException(args, e)
    }
    finally {
      Time.clear()
    }
  }

  private def handleException(args: MonitorUpdateArgs, e: RuntimeException): Unit = {
    val update = Json.string(args.update.printable())
    e match {
      case ve: ValidationException => log.info(s"ValidationException(${ve.getMessage}) $update")
      case _ => log.error(s"Could not update route: $update", e)
    }
    args.reporter.report(
      MonitorMessage(
        exception = Some(e.getMessage)
      )
    )
  }
}
