package kpn.server.repository

import kpn.core.app.stats.Figure
import kpn.core.database.Database
import kpn.core.database.views.analyzer.Overview
import org.springframework.stereotype.Component

@Component
class OverviewRepositoryImpl(analysisDatabase: Database) extends OverviewRepository {

  override def figures(stale: Boolean): Map[String, Figure] = {
    val result = Overview.query(analysisDatabase, stale)
    result.map(f => f.name -> f).toMap
  }

}
