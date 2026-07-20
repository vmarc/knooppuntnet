package kpn.api.common.network;

import kpn.api.common.Fact;
import kpn.api.common.LatLon;
import kpn.api.custom.Day;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NetworkNodeDetail(
  Long id,
  String name,
  String longName,
  String latitude,
  String longitude,
  Boolean connection,
  Boolean roleConnection,
  Boolean definedInRelation,
  Boolean proposed,
  Timestamp timestamp,
  Optional<Day> lastSurvey,
  Optional<Long> expectedRouteCount,
  ImmutableList<Fact> facts
) implements LatLon {
}

/* TODO migrate

  def toRef: Ref = {
    Ref(id, name)
  }

  def isSameAs(other: NetworkNodeDetail): Boolean = {
    name == other.name &&
      longName == other.longName &&
      latitude == other.latitude &&
      longitude == other.longitude &&
      connection == other.connection &&
      roleConnection == other.roleConnection &&
      definedInRelation == other.definedInRelation &&
      proposed == other.proposed &&
      lastSurvey == other.lastSurvey &&
      expectedRouteCount == other.expectedRouteCount &&
      facts == other.facts
  }
*/
