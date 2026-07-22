package kpn.server.analyzer.engine.analysis.route.structure

case class TestStructurePath(
  startNodeId: Long,
  endNodeId: Long,
  nodeIds: Seq[Long]
)
