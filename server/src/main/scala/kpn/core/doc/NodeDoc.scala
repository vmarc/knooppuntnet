package kpn.core.doc

import kpn.api.base.ObjectId
import kpn.api.base.WithId
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.LatLon
import kpn.api.common.NodeName
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.data.MetaData
import kpn.api.common.data.Tagable
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.Day
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class NodeDoc(
  _id: Long,
  labels: Seq[String],
  country: Option[Country],
  name: Option[String],
  names: Seq[NodeName],
  version: Long,
  changeSetId: Long,
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Seq[Tag],
  facts: Seq[Fact],
  locations: Seq[String],
  integrity: Option[NodeIntegrity] = None,
  routeReferences: Seq[Reference],
  networkRelationReferences: Seq[Reference], // networks with this node as a member (does not include node references in network routes only)
  stamp: Option[ObjectId],
) extends Tagable with LatLon with WithId {

  def active: Boolean = {
    labels.contains(Label.active)
  }

  def deactivated: NodeDoc = {
    copy(
      labels = labels.filterNot(label =>
        label == Label.active || label.startsWith("fact")
      )
    )
  }

  def toMeta: MetaData = {
    MetaData(
      version,
      lastUpdated,
      changeSetId
    )
  }

  def name(scopedRouteType: ScopedRouteType): String = {
    names.filter(_.scopedRouteType == scopedRouteType).map(_.name).mkString(" / ")
  }

  def longName(scopedRouteType: ScopedRouteType): String = {
    names.filter(_.scopedRouteType == scopedRouteType).flatMap(_.longName).mkString(" / ")
  }

  def routeTypeName(routeType: RouteType): String = {
    names.filter(_.routeType == routeType).map(_.name).mkString(" / ")
  }

  def isSameAs(other: NodeDoc): Boolean = {
    _id == other._id &&
      labels == other.labels &&
      country == other.country &&
      name == other.name &&
      names == other.names &&
      latitude == other.latitude &&
      longitude == other.longitude &&
      lastSurvey == other.lastSurvey &&
      tags == other.tags &&
      facts == other.facts
  }

  def nodeIntegrityDetail(scopedRouteType: ScopedRouteType): Option[NodeIntegrityDetail] = {
    integrity.toSeq.flatMap(_.details).find(_.hasScopedRouteType(scopedRouteType))
  }
}
