package kpn.core.database.views.location

import kpn.core.database.views.common.{Design, View}

object LocationDesign extends Design {
  val views: Seq[View] = Seq(
    LocationView
  )
}
