package kpn.server.analyzer.engine.analysis.route.structure

case class RouteAnalysisFragmentGroup(
  surface: String,
  fragments: Seq[RouteAnalysisFragment]
) {
  def nodeIds: Seq[Long] = fragments.head.nodeIds ++ fragments.tail.flatMap(_.nodeIds.tail)
}
