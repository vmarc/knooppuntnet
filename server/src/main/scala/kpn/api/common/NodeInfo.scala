package kpn.api.common

import kpn.api.common.common.ToStringBuilder
import kpn.api.common.data.Tagable
import kpn.api.common.location.Location
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
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
  tags: Tags,
  facts: Seq[Fact],
  location: Option[Location],
  tiles: Seq[String]
) extends Tagable with LatLon {

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

  def name(scopedNetworkType: ScopedNetworkType): String = {
    names.filter(_.scopedNetworkType == scopedNetworkType).map(_.name).mkString(" / ")
  }

  def subsets: Seq[Subset] = {
    country match {
      case Some(c) => names.map(_.scopedNetworkType.networkType).map(networkType => Subset(c, networkType))
      case None => Seq()
    }
  }

  def locations: Seq[String] = {
    location match {
      case Some(location) => location.names
      case None => Seq.empty
    }
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("id", id).
    field("active", active).
    field("orphan", orphan).
    field("country", country).
    field("name", name).
    field("names", names).
    field("latitude", latitude).
    field("longitude", longitude).
    field("lastUpdated", lastUpdated).
    field("tags", tags).
    field("facts", facts).
    field("tiles", tiles).
    build
}
