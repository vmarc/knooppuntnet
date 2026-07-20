package kpn.api.common.planner;

import kpn.api.common.LatLonImpl;
import kpn.api.common.planner.PlanCoordinate;

public record PlanFragment(
  Long meters,
  PlanCoordinate coordinate,
  LatLonImpl latLon
) {
}
