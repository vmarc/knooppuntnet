package kpn.api.common.planner

case class LegBuildParams(
  networkType: String,
  source: LegEnd,
  sink: LegEnd
) {

  def routeIds: Seq[Long] = (source.route.toSeq.map(_.routeId) ++ sink.route.toSeq.map(_.routeId)).distinct

}
