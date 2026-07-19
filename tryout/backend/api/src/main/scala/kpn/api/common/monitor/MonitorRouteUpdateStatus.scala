package kpn.api.common.monitor

case class MonitorRouteUpdateStatus(
  steps: Seq[MonitorRouteUpdateStep] = Seq.empty,
  done: Boolean = false,
  errors: Seq[String] = Seq.empty,
  exception: Option[String] = None
)
