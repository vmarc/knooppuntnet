package kpn.api.common;

import kpn.api.custom.Timestamp;

import java.util.Optional;

public record OrphanNodeInfo(
  Long id,
  String name,
  Optional<String> longName,
  Timestamp lastUpdated,
  Boolean proposed,
  Optional<String> lastSurvey,
  Long factCount
) {
}

/*
package kpn.api.common

import kpn.api.custom.Timestamp

case class OrphanNodeInfo(
  id: Long,
  name: String,
  longName: Option[String],
  lastUpdated: Timestamp,
  proposed: Boolean,
  lastSurvey: Option[String],
  factCount: Long,
)

*/
