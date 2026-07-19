package kpn.api.common.monitor;

import java.util.Optional;

public record MonitorRouteSubRelation(
  Optional<Long> subRelationIndex,
  Long relationId,
  String name
) {
}

/*
package kpn.api.common.monitor

case class MonitorRouteSubRelation(
  subRelationIndex: Option[Long],
  relationId: Long,
  name: String,
)

*/
