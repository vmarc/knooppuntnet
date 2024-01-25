package kpn.server.analyzer.engine.monitor.structure

case class Structure(
  forwardPath: Option[StructurePath],
  backwardPath: Option[StructurePath],
  otherPaths: Seq[StructurePath]
)