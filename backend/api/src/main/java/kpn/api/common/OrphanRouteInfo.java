package kpn.api.common;

import kpn.api.common.Fact;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record OrphanRouteInfo(
  Long id,
  String name,
  Long meters,
  Optional<String> lastSurvey,
  Timestamp lastUpdated,
  ImmutableList<Fact> facts,
  Boolean investigate
) {
}

/*
package kpn.api.common

import kpn.api.custom.Timestamp

case class OrphanRouteInfo(
  id: Long,
  name: String,
  meters: Long,
  lastSurvey: Option[String],
  lastUpdated: Timestamp,
  facts: Seq[Fact],
  investigate: Boolean
)

*/
