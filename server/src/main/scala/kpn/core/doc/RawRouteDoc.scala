package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.custom.Relation

case class RawRouteDoc(
  _id: Long,
  relation: Relation,
  subRelationTree: Option[RouteRelation]
) extends WithId
