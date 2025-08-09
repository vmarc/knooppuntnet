package kpn.core.tools.support

import kpn.api.common.Country
import kpn.api.common.RouteType

// TODO scala3 move back into using class
case class SpecialNode(nodeId: Long, country: Country, routeType: RouteType)
