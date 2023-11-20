package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage

trait MonitorUpdateReporter {

  def report(message: MonitorRouteUpdateStatusMessage): Unit

  def processList(processList: Seq[MonitorRouteRelation]): Unit = {
    val commands = processList.zipWithIndex.map { case (monitorRouteRelation, index) =>
      val description = s"${index + 1}/${processList.size} ${monitorRouteRelation.name}"
      MonitorRouteUpdateStatusCommand(
        "step-add",
        monitorRouteRelation.relationId.toString,
        Some(description)
      )
    } :+ MonitorRouteUpdateStatusCommand(
      "step-add",
      "save"
    )

    report(
      MonitorRouteUpdateStatusMessage(
        commands = commands
      )
    )
  }

  def stepActive(stepId: String): Unit = {
    report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-active", stepId)
        )
      )
    )
  }

  def stepDone(stepId: String): Unit = {
    report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-done", stepId)
        )
      )
    )
  }
}
