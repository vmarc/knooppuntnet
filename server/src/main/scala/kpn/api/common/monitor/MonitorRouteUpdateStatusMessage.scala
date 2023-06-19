package kpn.api.common.monitor

case class MonitorRouteUpdateStatusMessage(
  commands: Seq[MonitorRouteUpdateStatusCommand] = Seq.empty,
  errors: Option[Seq[String]] = None,
  exception: Option[String] = None
)
