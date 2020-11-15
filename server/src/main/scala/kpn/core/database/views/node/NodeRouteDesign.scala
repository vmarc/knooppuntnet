package kpn.core.database.views.node

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object NodeRouteDesign extends Design {
  val views: Seq[View] = Seq(
    NodeRouteView,
    NodeRouteReferenceView,
    NodeRouteExpectedView,
  )
}
