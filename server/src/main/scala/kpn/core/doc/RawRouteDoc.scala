package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.custom.Relation

case class RawRouteDoc(
  _id: Long,
  relation: Relation,
  structure: Option[RouteRelation]
) extends WithId
