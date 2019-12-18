package kpn.core.database.views.poi

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object PoiDesign extends Design {

  val views: Seq[View] = Seq(
    PoiView,
    PoiLayerView,
    PoiNodeIdView,
    PoiWayIdView,
    PoiRelationIdView,
    PoiTileView,
    PoiMultiLayerView
  )
}
