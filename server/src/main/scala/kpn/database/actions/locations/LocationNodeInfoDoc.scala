package kpn.database.actions.locations

import kpn.api.common.Fact
import kpn.api.common.NodeName
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class LocationNodeInfoDoc(
  id: Long,
  name: String,
  names: Seq[NodeName],
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Seq[Tag],
  facts: Seq[Fact],
  routeReferences: Seq[Reference]
) extends Tagable {

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
}
