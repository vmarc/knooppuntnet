package kpn.core.database.views.location

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object LocationDesign extends Design {
  val views: Seq[View] = Seq(
    LocationView,
    // LocationCountView,
    LocationRouteView,
    LocationNodeView,
    NodeRouteReferenceView
  )
}
