package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorRouteRelation

trait MonitorUpdateReporter {

  def report(message: MonitorMessage): Unit

  def processList(processList: Seq[MonitorRouteRelation]): Unit = {
    val processListSize = processList.size
    val commands = processList.zipWithIndex.map { case (monitorRouteRelation, index) =>
      val description = s"${index + 1}/$processListSize ${monitorRouteRelation.name}"
      MonitorCommand.add(monitorRouteRelation.relationId.toString, Some(description))
    } :+ MonitorCommand.add("save")

    report(
      MonitorMessage(commands)
    )
  }

  def stepActive(stepId: String): Unit = {
    report(
      MonitorMessage(
        MonitorCommand.active(stepId)
      )
    )
  }

  def stepDone(stepId: String): Unit = {
    report(
      MonitorMessage(
        MonitorCommand.done(stepId)
      )
    )
  }
}
