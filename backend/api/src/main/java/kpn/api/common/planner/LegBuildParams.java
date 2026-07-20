package kpn.api.common.planner;

import kpn.api.common.planner.LegEnd;
import kpn.core.doc.Storable;

public record LegBuildParams(
  String routeType,
  LegEnd source,
  LegEnd sink,
  Boolean proposed
) implements Storable {
}

/* TODO migrate

  def routeIds: Seq[Long] = {
    legEndRouteIds(source.route) ++ legEndRouteIds(sink.route).distinct
  }

  private def legEndRouteIds(legEndRoute: Option[LegEndRoute]): Seq[Long] = {
    legEndRoute.toSeq.flatMap(_.trackPathKeys).map(_.routeId)
  }

*/
