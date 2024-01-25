package kpn.server.analyzer.engine.monitor.structure

case class TestStructurePath(
  startNodeId: Long,
  endNodeId: Long,
  nodeIds: Seq[Long]
)
