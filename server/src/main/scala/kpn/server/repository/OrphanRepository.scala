package kpn.server.repository

import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Subset
import kpn.core.mongo.doc.NodeDoc

trait OrphanRepository {

  def orphanRoutes(subset: Subset): Seq[OrphanRouteInfo]

  def orphanRouteIds(subset: Subset): Seq[Long]

  def orphanNodes(subset: Subset): Seq[NodeDoc]

}
