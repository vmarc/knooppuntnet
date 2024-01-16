package kpn.server.analyzer.engine.monitor

case class RoutePath(
  startNodeId: Long,
  endNodeId: Long,
  elements: Seq[MonitorRouteElement] = Seq.empty,
)