package kpn.core.db.views

object AnalyzerDesign extends Design {

  val views: Seq[View] = Seq(
    DocumentView,
    FactView,
    GraphEdges,
    NetworkView,
    NetworkMapView,
    NodeNetworkReferenceView,
    NodeOrphanRouteReferenceView,
    OrphanRouteView,
    OrphanNodeView,
    Overview,
    ReferenceView,
    RouteFactView
  )
}
