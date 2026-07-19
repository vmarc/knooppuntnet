package kpn.api.common.planner;

import kpn.api.common.planner.LegEnd;
import kpn.api.common.planner.PlanRoute;

import com.google.common.collect.ImmutableList;

public record PlanLegDetail(
  LegEnd source,
  LegEnd sink,
  ImmutableList<PlanRoute> routes
) {
}

/*
package kpn.api.common.planner

case class PlanLegDetail(
  source: LegEnd,
  sink: LegEnd,
  routes: Seq[PlanRoute]
) {
  def meters: Long = routes.map(_.meters).sum
}

*/
