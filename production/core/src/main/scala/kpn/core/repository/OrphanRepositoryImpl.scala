package kpn.core.repository

import akka.util.Timeout
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.OrphanNodeView
import kpn.core.db.views.OrphanRouteView
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.NodeInfo
import kpn.shared.RouteSummary
import kpn.shared.Subset

class OrphanRepositoryImpl(database: Database) extends OrphanRepository {

  private val log = Log(classOf[OrphanRepositoryImpl])

  override def orphanRoutes(subset: Subset, timeout: Timeout): Seq[RouteSummary] = {
    val country = subset.country.domain
    val networkType = subset.networkType.name
    val id = s"orphan_routes_${country}_$networkType"
    database.query(AnalyzerDesign, OrphanRouteView, Couch.uiTimeout)(false, true, true, country, networkType).map(OrphanRouteView.convert)
  }

  override def orphanNodes(subset: Subset, timeout: Timeout): Seq[NodeInfo] = {
    val country = subset.country.domain
    val networkType = subset.networkType.name
    val id = s"orphan_nodes_${country}_$networkType"
    database.query(AnalyzerDesign, OrphanNodeView, Couch.uiTimeout)(false, true, true, country, networkType).map(OrphanNodeView.convert)
  }

  override def ignoredRouteIds(networkType: NetworkType): Seq[Long] = {
    database.groupQuery(6, AnalyzerDesign, OrphanRouteView, Couch.batchTimeout)(true, true).map(OrphanRouteView.toObjectId)
  }

  override def allIgnoredRouteIds(): Seq[Long] = {
    database.groupQuery(6, AnalyzerDesign, OrphanRouteView, Couch.batchTimeout)(true).map(OrphanRouteView.toObjectId)
  }

  override def ignoredNodeIds(networkType: NetworkType): Seq[Long] = {
    database.groupQuery(6, AnalyzerDesign, OrphanNodeView, Couch.batchTimeout)(true, true, "", "", networkType.name).map(OrphanRouteView.toObjectId)
  }

  override def allIgnoredNodeIds(): Seq[Long] = {
    database.groupQuery(6, AnalyzerDesign, OrphanNodeView, Couch.batchTimeout)(true).map(OrphanRouteView.toObjectId)
  }
}
