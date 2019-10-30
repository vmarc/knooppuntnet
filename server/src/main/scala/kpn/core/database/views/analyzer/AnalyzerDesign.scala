package kpn.core.database.views.analyzer

import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object AnalyzerDesign extends Design {

  val views: Seq[View] = Seq(
    DocumentView,
    FactsPerNetworkView,
    FactView, // not in use
    NetworkView,
    NetworkMapView, // not in use
    NodeNetworkReferenceView,
    NodeOrphanRouteReferenceView,
    OrphanRouteView,
    OrphanNodeView,
    Overview,
    ReferenceView
  )
}
