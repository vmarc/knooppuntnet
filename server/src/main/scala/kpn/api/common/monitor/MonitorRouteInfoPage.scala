package kpn.api.common.monitor

case class MonitorRouteInfoPage(
  relationId: Long,
  name: Option[String] = None,
  ref: Option[String] = None,
  from: Option[String] = None,
  to: Option[String] = None,
  operator: Option[String] = None,
  website: Option[String] = None,
  symbol: Option[String] = None
)
