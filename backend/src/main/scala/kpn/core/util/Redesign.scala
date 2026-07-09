package kpn.core.util

import kpn.api.common.RouteType

object Redesign {
  val enableNewFactTests = false
  val enableDebugPrinting = false
  val tileGenerationRouteTypes: Seq[RouteType] = Seq(RouteType.hiking) // routeType.all // Seq.empty
}
