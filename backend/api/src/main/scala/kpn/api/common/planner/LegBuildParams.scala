package kpn.api.common.planner

import kpn.api.id.Storable

case class LegBuildParams(
  routeType: String,
  source: LegEnd,
  sink: LegEnd,
  proposed: Boolean
) extends Storable {

  def routeIds: Seq[Long] = {
    legEndRouteIds(source.route) ++ legEndRouteIds(sink.route).distinct
  }

  private def legEndRouteIds(legEndRoute: Option[LegEndRoute]): Seq[Long] = {
    legEndRoute.toSeq.flatMap(_.trackPathKeys).map(_.routeId)
  }
}
