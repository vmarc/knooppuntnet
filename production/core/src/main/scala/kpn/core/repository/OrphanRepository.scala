package kpn.core.repository

import akka.util.Timeout
import kpn.core.db.couch.Couch
import kpn.shared.NetworkType
import kpn.shared.NodeInfo
import kpn.shared.RouteSummary
import kpn.shared.Subset

trait OrphanRepository {

  def orphanRoutes(subset: Subset, timeout: Timeout = Couch.defaultTimeout): Seq[RouteSummary]

  def orphanNodes(subset: Subset, timeout: Timeout = Couch.defaultTimeout): Seq[NodeInfo]

  def ignoredRouteIds(networkType: NetworkType): Seq[Long]

  def allIgnoredRouteIds(): Seq[Long]

  def ignoredNodeIds(networkType: NetworkType): Seq[Long]

  def allIgnoredNodeIds(): Seq[Long]
}
