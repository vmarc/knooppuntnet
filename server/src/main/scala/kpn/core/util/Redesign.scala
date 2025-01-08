package kpn.core.util

import kpn.api.common.RouteType

object Redesign {
  val enableNewFactTests = false
  val enableDebugPrinting = true
  val enablePendingTests = false
  val tileGenerationRouteTypes: Seq[RouteType] = Seq(RouteType.hiking) // routeType.all // Seq.empty
}
