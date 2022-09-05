package kpn.api.common.monitor

case class MonitorRouteSaveResult(
  analyzed: Boolean = false,
  errors: Seq[String] = Seq.empty
)
