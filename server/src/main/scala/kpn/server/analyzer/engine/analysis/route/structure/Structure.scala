package kpn.server.analyzer.engine.analysis.route.structure

case class Structure(
  forwardPath: Option[StructurePath],
  backwardPath: Option[StructurePath],
  startTentaclePaths: Seq[StructurePath],
  endTentaclePaths: Seq[StructurePath],
  otherPaths: Seq[StructurePath]
) {
  def nodeNetworkPaths: Seq[StructurePath] = {
    // does not include otherPaths
    forwardPath.toSeq ++ backwardPath.toSeq ++ startTentaclePaths ++ endTentaclePaths
  }

  def allPaths: Seq[StructurePath] = {
    nodeNetworkPaths ++ otherPaths
  }
}
