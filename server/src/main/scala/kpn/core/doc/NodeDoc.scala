package kpn.core.doc

import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.data.Tagable
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tag
import org.bson.types.ObjectId

case class NodeDoc(
  _id: Long,
  active: Boolean,
  base: NodeBaseData,
  labels: Seq[String],
  facts: Seq[Fact],
  integrity: Option[NodeIntegrity] = None,
  routeReferences: Seq[Reference],
  networkRelationReferences: Seq[Reference], // networks with this node as a member (does not include node references in network routes only)
  stamp: Option[ObjectId],
) extends Tagable with WithId {

  def tags: Seq[Tag] = {
    base.raw.tags
  }

  def deactivated: NodeDoc = {
    copy(
      active = false
    )
  }

  def name(scopedRouteType: ScopedRouteType): String = {
    base.names.filter(_.scopedRouteType == scopedRouteType).map(_.name).mkString(" / ")
  }

  def longName(scopedRouteType: ScopedRouteType): String = {
    base.names.filter(_.scopedRouteType == scopedRouteType).flatMap(_.longName).mkString(" / ")
  }

  def routeTypeName(routeType: RouteType): String = {
    base.names.filter(_.routeType == routeType).map(_.name).mkString(" / ")
  }

  def isSameAs(other: NodeDoc): Boolean = {
    _id == other._id &&
      active == other.active &&
      base == other.base &&
      labels == other.labels &&
      facts == other.facts
  }

  def nodeIntegrityDetail(scopedRouteType: ScopedRouteType): Option[NodeIntegrityDetail] = {
    integrity.toSeq.flatMap(_.details).find(_.hasScopedRouteType(scopedRouteType))
  }
}
