package kpn.core.database.views.planner

import kpn.core.database.views.common.{Design, View}

object PlannerDesign extends Design {
  val views: Seq[View] = Seq(
    GraphEdgesView
  )
}
