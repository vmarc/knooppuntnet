package kpn.api.common.monitor;

import kpn.api.common.Bounds;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorGroupsPageGroup(
  String id,
  String name,
  String description,
  Long routeCount,
  ImmutableList<String> monitorRouteIds,
  Optional<Bounds> bounds
) {
}

/*
package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorGroupsPageGroup(
  id: String,
  name: String,
  description: String,
  routeCount: Long,
  monitorRouteIds: Seq[String],
  bounds: Option[Bounds]
)

*/
