package kpn.core.db.views

object AnalyzerDesign extends Design {

  val views: Seq[View] = Seq(
    DocumentView,
    FactView,
    NetworkView,
    NetworkMapView,
    NewReferenceView,
    NodeOrphanRouteReferenceView,
    Overview,
    GraphEdges,
    ReferenceView,
    RouteFactView,
    OrphanRouteView,
    OrphanNodeView
  )
}
