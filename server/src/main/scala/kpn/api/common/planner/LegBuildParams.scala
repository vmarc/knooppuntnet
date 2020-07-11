package kpn.api.common.planner

case class LegBuildParams(
  networkType: String,
  source: LegEnd,
  sink: LegEnd
) {

  def routeIds: Seq[Long] = {
    legEndRouteIds(source.route) ++ legEndRouteIds(sink.route).distinct
  }

  private def legEndRouteIds(legEndRoute: Option[LegEndRoute]): Seq[Long] = {
    legEndRoute.toSeq.flatMap(_.trackPathKeys).map(_.routeId)
  }

}
