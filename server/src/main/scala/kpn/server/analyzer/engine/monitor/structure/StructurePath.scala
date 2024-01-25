package kpn.server.analyzer.engine.monitor.structure

case class StructurePath(
  startNodeId: Long,
  endNodeId: Long,
  elements: Seq[StructureElement] = Seq.empty,
)