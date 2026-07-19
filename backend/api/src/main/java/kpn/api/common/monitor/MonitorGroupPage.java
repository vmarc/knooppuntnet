package kpn.api.common.monitor;

import kpn.api.common.Bounds;
import kpn.api.common.monitor.MonitorRouteDetail;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorGroupPage(
  Boolean adminUser,
  String groupId,
  String groupName,
  String groupDescription,
  Optional<Bounds> bounds,
  ImmutableList<Long> relationIds,
  ImmutableList<MonitorRouteDetail> routes
) {
}

/*
package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorGroupPage(
  adminUser: Boolean,
  groupId: String,
  groupName: String,
  groupDescription: String,
  bounds: Option[Bounds],
  relationIds: Seq[Long],
  routes: Seq[MonitorRouteDetail]
)

*/
