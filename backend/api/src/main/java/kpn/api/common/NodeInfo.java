package kpn.api.common;

import kpn.api.common.data.Tagable;
import kpn.api.common.node.NodeIntegrity;
import kpn.api.custom.Day;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NodeInfo(
  Long id,
  Boolean active,
  Boolean orphan,
  Optional<Country> country,
  String name,
  ImmutableList<NodeName> names,
  String latitude,
  String longitude,
  Timestamp lastUpdated,
  Optional<Day> lastSurvey,
  ImmutableList<Tag> tags,
  ImmutableList<Fact> facts,
  ImmutableList<LocationInfo> locations,
  Optional<NodeIntegrity> integrity
) implements Tagable, LatLon {}

/* TODO migrate

  def routeTypeName(routeType: RouteType): String = {
    names.filter(_.routeType == routeType).map(_.name).mkString(" / ")
  }

  def routeTypeLongName(routeType: RouteType): Option[String] = {
    val longNames = names.filter(_.routeType == routeType).flatMap(_.longName)
    Option.when(longNames.nonEmpty) {
      longNames.mkString(" / ")
    }
  }

  def routeTypeProposed(routeType: RouteType): Boolean = {
    names.filter(_.routeType == routeType).exists(_.proposed)
  }

  def name(scopedRouteType: ScopedRouteType): String = {
    names.filter(_.scopedRouteType == scopedRouteType).map(_.name).mkString(" / ")
  }

  def longName(scopedRouteType: ScopedRouteType): String = {
    names.filter(_.scopedRouteType == scopedRouteType).flatMap(_.longName).mkString(" / ")
  }

  def subsets: Seq[Subset] = {
    country match {
      case Some(c) => names.map(_.scopedRouteType.routeType).map(routeType => Subset(c, routeType))
      case None => Seq.empty
    }
  }

*/
