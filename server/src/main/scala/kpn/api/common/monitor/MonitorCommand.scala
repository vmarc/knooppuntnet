package kpn.api.common.monitor

object MonitorCommand {

  def add(stepId: String, description: Option[String] = None): MonitorCommand = {
    MonitorCommand(MonitorCommandAction.stepAdd, stepId, description)
  }

  def active(stepId: String, description: Option[String] = None): MonitorCommand = {
    MonitorCommand(MonitorCommandAction.stepActive, stepId, description)
  }

  def done(stepId: String, description: Option[String] = None): MonitorCommand = {
    MonitorCommand(MonitorCommandAction.stepDone, stepId, description)
  }
}

case class MonitorCommand(
  action: MonitorCommandAction,
  stepId: String, // "prepare", "analyze-route-structure", subrelationId, "upload", , "delete"
  description: Option[String] = None
)
