package kpn.api.common

import kpn.api.base.WithId
import kpn.api.common.common.Reference
import kpn.api.common.data.Tagable
import kpn.api.common.node.NodeIntegrity
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class NodeInfo(
  _id: Long,
  id: Long, // TODO MONGO remove after migration
  labels: Seq[String],
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
  locations: Seq[String],
  tiles: Seq[String],
  integrity: Option[NodeIntegrity],
  routeReferences: Seq[Reference]
) extends Tagable with LatLon with WithId {

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

  def longName(scopedNetworkType: ScopedNetworkType): String = {
    names.filter(_.scopedNetworkType == scopedNetworkType).flatMap(_.longName).mkString(" / ")
  }

  def subsets: Seq[Subset] = {
    country match {
      case Some(c) => names.map(_.scopedNetworkType.networkType).map(networkType => Subset(c, networkType))
      case None => Seq.empty
    }
  }
}
