package kpn.api.common.location;

import kpn.api.common.Fact;
import kpn.api.custom.Day;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record LocationRouteInfo(
  Long rowIndex,
  Long id,
  String name,
  Long meters,
  Timestamp lastUpdated,
  Optional<Day> lastSurvey,
  Optional<String> symbol,
  Boolean proposed,
  ImmutableList<Fact> facts
) {
}

/*
package kpn.api.common.location

import kpn.api.common.Fact
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class LocationRouteInfo(
  rowIndex: Long,
  id: Long,
  name: String,
  meters: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  symbol: Option[String],
  proposed: Boolean,
  facts: Seq[Fact]
)

*/
