package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.core.util.ValidationException
import kpn.server.json.Json
import org.springframework.stereotype.Component

@Component
class MonitorRouteUpdateExecutor(
  monitorAdd: MonitorAdd,
  monitorUpdate: MonitorUpdate,
  monitorGpxUpload: MonitorGpxUpload,
  monitorGpxDelete: MonitorGpxDelete,
) {

  private val log = Log(classOf[MonitorRouteUpdateExecutor])

  private val actionStrategies: Map[MonitorAction, MonitorContext => Unit] = Map(
    MonitorAction.add -> monitorAdd.execute,
    MonitorAction.update -> monitorUpdate.execute,
    MonitorAction.gpxUpload -> monitorGpxUpload.execute,
    MonitorAction.gpxDelete -> monitorGpxDelete.execute
  )

  def execute(originalContext: MonitorUpdateContext): Unit = {
    val context = initContext(originalContext)
    try {
      val action = originalContext.update.action
      actionStrategies.get(action) match {
        case Some(strategy) => strategy(context)
        case None => throw new IllegalArgumentException(s"Unknown action type: $action")
      }
    }
    catch {
      case e: RuntimeException =>
        handleException(context, e)
    }
    finally {
      Time.clear()
    }
  }

  private def initContext(originalContext: MonitorUpdateContext): MonitorContext = {
    val context = new MonitorContext()
    context.set(
      originalContext.copy(
        referenceType = Some(originalContext.update.referenceType),
        analysisStartMillis = Some(System.currentTimeMillis())
      )
    )
    context
  }

  private def handleException(context: MonitorContext, e: RuntimeException): Unit = {
    val update = Json.string(context.value.update.printable())
    e match {
      case ve: ValidationException => log.info(s"ValidationException(${ve.getMessage}) $update")
      case _ => log.error(s"Could not update route: $update", e)
    }
    context.value.reporter.report(
      MonitorRouteUpdateStatusMessage(
        exception = Some(e.getMessage)
      )
    )
  }
}
