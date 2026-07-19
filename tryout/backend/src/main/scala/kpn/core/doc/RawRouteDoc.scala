package kpn.core.doc

import kpn.api.common.Relation

case class RawRouteDoc(
  _id: Long,
  relation: Relation,
  subRelationTree: Option[RouteRelation]
) extends WithId
