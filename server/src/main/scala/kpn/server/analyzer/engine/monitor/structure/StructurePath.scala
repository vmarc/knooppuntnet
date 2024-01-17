package kpn.server.analyzer.engine.monitor.structure

case class StructurePath(
  startNodeId: Long,
  endNodeId: Long,
  elements: Seq[MonitorRouteElement] = Seq.empty,
)