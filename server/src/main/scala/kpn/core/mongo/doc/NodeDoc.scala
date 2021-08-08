package kpn.core.mongo.doc

import kpn.api.base.WithId
import kpn.api.common.LatLon
import kpn.api.common.NodeName
import kpn.api.common.data.Tagable
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class NodeDoc(
  _id: Long,
  labels: Seq[String],
  active: Boolean,
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
  tiles: Seq[String]
) extends Tagable with LatLon with WithId {

  def name(scopedNetworkType: ScopedNetworkType): String = {
    names.filter(_.scopedNetworkType == scopedNetworkType).map(_.name).mkString(" / ")
  }

  def longName(scopedNetworkType: ScopedNetworkType): String = {
    names.filter(_.scopedNetworkType == scopedNetworkType).flatMap(_.longName).mkString(" / ")
  }

  def networkTypeName(networkType: NetworkType): String = {
    names.filter(_.networkType == networkType).map(_.name).mkString(" / ")
  }



}
