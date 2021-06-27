package kpn.server.repository

import kpn.core.app.stats.Figure
import kpn.core.mongo.Database
import kpn.core.database.views.analyzer.Overview
import org.springframework.stereotype.Component

@Component
class OverviewRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends OverviewRepository {

  override def figures(stale: Boolean): Map[String, Figure] = {
    if (mongoEnabled) {
      ???
    }
    else {
      val result = Overview.query(analysisDatabase, stale)
      result.map(f => f.name -> f).toMap
    }
  }
}
