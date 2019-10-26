package kpn.server.repository

import akka.util.Timeout
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.database.views.analyzer.{AnalyzerDesign, OrphanNodeView, OrphanRouteView}
import kpn.shared.NodeInfo
import kpn.shared.RouteSummary
import kpn.shared.Subset
import org.springframework.stereotype.Component

@Component
class OrphanRepositoryImpl(analysisDatabase: Database) extends OrphanRepository {

  override def orphanRoutes(subset: Subset, timeout: Timeout = Couch.defaultTimeout): Seq[RouteSummary] = {
    val country = subset.country.domain
    val networkType = subset.networkType.name
    analysisDatabase.old.query(AnalyzerDesign, OrphanRouteView, timeout)(false, true, true, country, networkType).map(OrphanRouteView.convert)
  }

  override def orphanNodes(subset: Subset, timeout: Timeout = Couch.defaultTimeout): Seq[NodeInfo] = {
    val country = subset.country.domain
    val networkType = subset.networkType.name
    analysisDatabase.old.query(AnalyzerDesign, OrphanNodeView, timeout)(false, true, true, country, networkType).map(OrphanNodeView.convert)
  }
}
