package kpn.core.tools.next.domain

import kpn.api.base.WithId

case class OldNextRouteRelation(
  _id: Long,
  relation: OldRelation
) extends WithId
