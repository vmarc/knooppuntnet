package kpn.database.actions.locations

import kpn.api.common.NodeName
import kpn.api.common.common.Reference
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
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

  def networkTypeName(networkType: NetworkType): String = {
    names.filter(_.networkType == networkType).map(_.name).mkString(" / ")
  }

  def networkTypeLongName(networkType: NetworkType): Option[String] = {
    val longNames = names.filter(_.networkType == networkType).flatMap(_.longName)
    if (longNames.nonEmpty) {
      Some(longNames.mkString(" / "))
    }
    else {
      None
    }
  }
}
