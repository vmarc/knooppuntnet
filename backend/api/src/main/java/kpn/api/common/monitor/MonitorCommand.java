package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorCommandAction;

import java.util.Optional;

public record MonitorCommand(
  MonitorCommandAction action,
  String stepId,
  Optional<String> description
) {
}

/* TODO migrate
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

*/
