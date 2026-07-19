package kpn.api.common.monitor;

import kpn.api.common.Bounds;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorRouteDetail(
  Long rowIndex,
  String routeId,
  String name,
  String description,
  Optional<String> symbol,
  Optional<Long> relationId,
  String referenceType,
  Optional<Timestamp> referenceTimestamp,
  Long referenceDistance,
  Long deviationDistance,
  Long deviationCount,
  Long osmSegmentCount,
  ImmutableList<Long> relationIds,
  Optional<Bounds> bounds,
  Boolean happy
) {
}

/*
package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.custom.Timestamp

case class MonitorRouteDetail(
  rowIndex: Long,
  routeId: String,
  name: String,
  description: String,
  symbol: Option[String],
  relationId: Option[Long],
  referenceType: String,
  referenceTimestamp: Option[Timestamp],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmSegmentCount: Long,
  relationIds: Seq[Long],
  bounds: Option[Bounds],
  happy: Boolean
)

*/
