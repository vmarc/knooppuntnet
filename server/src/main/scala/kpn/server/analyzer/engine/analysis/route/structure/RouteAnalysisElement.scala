package kpn.server.analyzer.engine.analysis.route.structure

case class RouteAnalysisElement(
  id: Long,
  direction: RoutePathDirection,
  fromNetworkNode: Option[RouteAnalysisNode],
  toNetworkNode: Option[RouteAnalysisNode],
  fromNodeId: Long,
  toNodeId: Long,
  fragmentGroups: Seq[RouteAnalysisFragmentGroup]
) {
  def fragments: Seq[RouteAnalysisFragment] = {
    fragmentGroups.flatMap(_.fragments)
  }

  def nodeIds: Seq[Long] = {
    fragments.headOption match {
      case Some(firstFragment) => firstFragment.nodeIds ++ fragments.tail.flatMap(_.nodeIds.tail)
      case None => Seq.empty
    }
  }
}
