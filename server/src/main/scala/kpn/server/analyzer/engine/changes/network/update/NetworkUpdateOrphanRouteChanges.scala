package kpn.server.analyzer.engine.changes.network.update

import kpn.shared.common.Ref

case class NetworkUpdateOrphanRouteChanges(
  oldOrphanRoutes: Seq[Ref],
  oldIgnoredRoutes: Seq[Ref],
  newOrphanRoutes: Seq[Ref],
  newIgnoredRoutes: Seq[Ref]
)
