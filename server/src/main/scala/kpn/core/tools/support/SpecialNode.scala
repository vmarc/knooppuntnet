package kpn.core.tools.support

import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class SpecialNode(
  nodeId: Long,
  country: Country,
  routeType: RouteType
) extends Storable
