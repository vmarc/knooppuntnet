package kpn.api.common

import kpn.api.common.common.Reference
import kpn.api.common.data.Tagable
import kpn.api.common.node.NodeIntegrity
import kpn.api.custom.Day
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Subset
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class NodeInfo(
  id: Long,
  active: Boolean,
  orphan: Boolean,
  country: Option[Country],
  name: String,
  names: Seq[NodeName],
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Seq[Tag],
  facts: Seq[Fact],
  locations: Seq[LocationInfo],
  tiles: Seq[String],
  integrity: Option[NodeIntegrity],
  routeReferences: Seq[Reference]
) extends Tagable with LatLon {
  def routeTypeName(routeType: RouteType): String = {
    names.filter(_.routeType == routeType).map(_.name).mkString(" / ")
  }

  def routeTypeLongName(routeType: RouteType): Option[String] = {
    val longNames = names.filter(_.routeType == routeType).flatMap(_.longName)
    if (longNames.nonEmpty) {
      Some(longNames.mkString(" / "))
    }
    else {
      None
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
}
