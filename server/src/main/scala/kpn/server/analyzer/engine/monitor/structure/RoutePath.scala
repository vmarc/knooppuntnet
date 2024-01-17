package kpn.server.analyzer.engine.monitor.structure

case class RoutePath(
  startNodeId: Long,
  endNodeId: Long,
  elements: Seq[MonitorRouteElement] = Seq.empty,
)