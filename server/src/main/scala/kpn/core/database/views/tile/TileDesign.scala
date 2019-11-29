package kpn.core.database.views.tile

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object TileDesign extends Design {

  val views: Seq[View] = Seq(
    TileView
  )
}
