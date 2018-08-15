package kpn.core.repository

import akka.util.Timeout
import kpn.core.app.stats.Figure
import kpn.core.db.couch.Database
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.Overview

class OverviewRepositoryImpl(database: Database) extends OverviewRepository {

  override def figures(timeout: Timeout, stale: Boolean): Map[String, Figure] = {
    database.groupQuery(1, AnalyzerDesign, Overview, timeout, stale)().map(Overview.convert).map(f => f.name -> f).toMap
  }

  override def figure(timeout: Timeout, factName: String, stale: Boolean): Option[Figure] = {
    val rows = database.query(Overview, timeout, stale)(factName).map(Overview.convert)
    rows match {
      case Seq(figure) => Some(figure)
      case _ => None
    }
  }
}
