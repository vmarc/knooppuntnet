package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.RouteSummary
import kpn.api.custom.Subset

trait OrphanRepository {

  def orphanRoutes(subset: Subset): Seq[RouteSummary]

  def orphanNodes(subset: Subset): Seq[NodeInfo]

}
