package kpn.core.database.views.analyzer

import kpn.core.database.views._
import kpn.core.database.views.common.{Design, View}

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
