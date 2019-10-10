package kpn.core.db.views

object AnalyzerDesign extends Design {

  val views: Seq[View] = Seq(
    DocumentView,
    FactsPerNetworkView,
    FactView,
    //GraphEdges,
    NetworkView,
    NetworkMapView,
    NodeNetworkReferenceView,
    NodeOrphanRouteReferenceView,
    OrphanRouteView,
    OrphanNodeView,
    Overview,
    ReferenceView
  )
}
