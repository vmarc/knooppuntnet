package kpn.core.database.views.monitor

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object MonitorDesign extends Design {

  val views: Seq[View] = Seq(
    MonitorRouteView
  )
}
