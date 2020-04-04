package kpn.core.database.views.metrics

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object BackendMetricsDesign extends Design {
  val views: Seq[View] = Seq(
    BackendMetricsView
  )
}
