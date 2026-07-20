package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorReferenceType;
import kpn.api.custom.Timestamp;

import java.util.Optional;

public record MonitorRouteProperties(
  String groupName,
  String name,
  String description,
  Optional<String> comment,
  Optional<Long> relationId,
  MonitorReferenceType referenceType,
  Optional<Timestamp> referenceTimestamp,
  Optional<String> referenceFilename,
  Boolean referenceFileChanged
) {
}

/* TODO migrate
package kpn.api.common.monitor

import kpn.api.custom.Timestamp

case class MonitorRouteProperties(
  groupName: String,
  name: String,
  description: String,
  comment: Option[String],
  relationId: Option[Long],
  referenceType: MonitorReferenceType,
  referenceTimestamp: Option[Timestamp],
  referenceFilename: Option[String],
  referenceFileChanged: Boolean,
)

*/
