package kpn.core.database.views.poi

import kpn.core.database.views.common.{Design, View}

object PoiDesign extends Design {

  val views: Seq[View] = Seq(
    PoiView,
    LayerView
  )
}
