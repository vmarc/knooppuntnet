package kpn.core.doc

import kpn.api.common.Bounds
import kpn.api.common.Fact
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.data.Tagable
import kpn.api.common.route.ParentRoute
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.common.route.RouteStructureRow
import kpn.api.custom.Tag
import org.bson.types.ObjectId

case class RouteDoc(
  _id: Long, // routeId
  active: Boolean,
  labels: Seq[String],
  base: RouteBaseData,
  facts: Seq[Fact],
  unexpectedRelationIds: Seq[Long],
  segments: Seq[RouteSegment],
  superDistance: Long,
  superSegments: Seq[SuperSegment],
  paths: Seq[RoutePath],
  routeIds: Seq[Long], // routeId of this route plus all other routes in the entire tree
  structureRows: Seq[RouteStructureRow],
  relationCount: Long,
  relationLevels: Long,
  parentRoutes: Seq[ParentRoute],
  networkReferences: Seq[Reference],
  bounds: Option[Bounds],
  stamp: Option[ObjectId],
) extends WithId with Tagable {

  def toRef: Ref = Ref(_id, base.name)

  def deactivated: RouteDoc = {
    copy(
      active = false,
      labels = labels.filterNot(_.startsWith("fact"))
    )
  }

  def tags: Seq[Tag] = {
    base.raw.tags
  }
}
