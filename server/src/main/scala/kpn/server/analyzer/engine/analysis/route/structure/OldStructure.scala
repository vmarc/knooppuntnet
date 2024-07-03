package kpn.server.analyzer.engine.analysis.route.structure

case class OldStructure(
  forwardPath: Option[OldStructurePath],
  backwardPath: Option[OldStructurePath],
  otherPaths: Seq[OldStructurePath]
)
