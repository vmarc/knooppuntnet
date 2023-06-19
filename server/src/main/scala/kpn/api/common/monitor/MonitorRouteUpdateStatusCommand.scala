package kpn.api.common.monitor

case class MonitorRouteUpdateStatusCommand(
  action: String, // "step-add", "step-active", "step-done"
  stepId: String, // "prepare", "analyze-route-structure", subrelationId
  description: Option[String] = None
)
