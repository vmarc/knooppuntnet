package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.changes.details.RouteChange
import kpn.core.doc.Storable

case class RouteChangeContext(
  routeChange: RouteChange,
  impactedNodeIds: Seq[Long],
  impactedNetworkIds: Seq[Long]
) extends Storable
