package kpn.api.common.monitor;

import kpn.api.custom.Timestamp;

import java.util.Optional;

public record MonitorRouteGpxPage(
  String groupName,
  String routeName,
  Long subRelationId,
  String subRelationDescription,
  Timestamp referenceTimestamp,
  Optional<String> referenceFilename,
  Long referenceDistance
) {
}

/*
package kpn.api.common.monitor

import kpn.api.custom.Timestamp

case class MonitorRouteGpxPage(
  groupName: String,
  routeName: String,
  subRelationId: Long,
  subRelationDescription: String,
  referenceTimestamp: Timestamp,
  referenceFilename: Option[String],
  referenceDistance: Long
)

*/
