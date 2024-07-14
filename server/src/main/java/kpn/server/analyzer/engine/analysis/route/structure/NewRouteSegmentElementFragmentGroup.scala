package kpn.server.analyzer.engine.analysis.route.structure

case class NewRouteSegmentElementFragmentGroup(
  surface: String,
  fragments: Seq[NewRouteSegmentElementFragment]
) {
  def nodeIds: Seq[Long] = fragments.head.nodeIds ++ fragments.tail.flatMap(_.nodeIds.tail)
}
