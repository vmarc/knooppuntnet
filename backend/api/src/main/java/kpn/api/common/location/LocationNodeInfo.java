package kpn.api.common.location;

import kpn.api.common.Fact;
import kpn.api.common.common.Reference;
import kpn.api.custom.Day;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record LocationNodeInfo(
  Long rowIndex,
  Long id,
  String name,
  String longName,
  String latitude,
  String longitude,
  Timestamp lastUpdated,
  Optional<Day> lastSurvey,
  ImmutableList<Fact> facts,
  Optional<Long> expectedRouteCount,
  ImmutableList<Reference> routeReferences
) {
}

/*
package kpn.api.common.location

import kpn.api.common.Fact
import kpn.api.common.LatLon
import kpn.api.common.common.Reference
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class LocationNodeInfo(
  rowIndex: Long,
  id: Long,
  name: String,
  longName: String,
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  expectedRouteCount: Option[Long],
  routeReferences: Seq[Reference]
) extends LatLon

*/
