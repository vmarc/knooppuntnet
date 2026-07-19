package kpn.api.common.planner;

import kpn.api.common.LatLonImpl;
import kpn.api.common.planner.PlanCoordinate;

public record PlanFragmentCoordinate(
  PlanCoordinate coordinate,
  LatLonImpl latLon
) {
}

/*
package kpn.api.common.planner

import kpn.api.common.LatLonImpl

case class PlanFragmentCoordinate(
  coordinate: PlanCoordinate,
  latLon: LatLonImpl
)

*/
