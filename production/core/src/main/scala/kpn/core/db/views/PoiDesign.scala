package kpn.core.db.views

object PoiDesign extends Design {

  val views: Seq[View] = Seq(
    PoiView,
    LayerView
  )
}
