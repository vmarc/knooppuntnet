package kpn.api.common.monitor

case class MonitorRouteUpdateStep(
  name: String,
  status: String = "todo" // "todo" | "busy" | "done"
)
