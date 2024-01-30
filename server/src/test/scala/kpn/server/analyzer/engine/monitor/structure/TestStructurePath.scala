package kpn.server.analyzer.engine.monitor.structure

case class StructurePath(
  startNodeId: Long,
  endNodeId: Long,
  nodeIds: Seq[Long]
)
