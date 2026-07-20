package kpn.api.common.planner;

import kpn.api.common.LatLonImpl;
import kpn.api.common.planner.PlanCoordinate;

import java.util.Optional;

public record PlanNode(
  String featureId, // TODO PLAN has become obsolete ??? remove ???
  String nodeId,
  String nodeName,
  Optional<String> nodeLongName,
  PlanCoordinate coordinate,
  LatLonImpl latLon // TODO PLAN has become obsolete? NO: used to create GPX on client ==> YES: zou lokaal terug van Coordinate berekend kunnen worden...
) {
}
