package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.NodeInfo
import kpn.api.common.RouteSummary
import kpn.api.custom.Subset
import kpn.core.db.couch.Couch

trait OrphanRepository {

  def orphanRoutes(subset: Subset, timeout: Timeout = Couch.defaultTimeout): Seq[RouteSummary]

  def orphanNodes(subset: Subset, timeout: Timeout = Couch.defaultTimeout): Seq[NodeInfo]

}
