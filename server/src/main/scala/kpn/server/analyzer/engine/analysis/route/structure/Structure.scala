package kpn.server.analyzer.engine.analysis.route.structure

case class Structure(
  forwardPath: Option[StructurePath],
  backwardPath: Option[StructurePath],
  otherPaths: Seq[StructurePath]
)
