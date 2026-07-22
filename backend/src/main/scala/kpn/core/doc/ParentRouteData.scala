package kpn.core.doc

import kpn.api.id.Storable

case class ParentRouteData(
  routeId: Long,
  name: String
) extends Storable
