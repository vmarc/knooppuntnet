package kpn.server.analyzer.engine

import javax.annotation.PostConstruct
import kpn.api.common.status.PeriodParameters
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.views.analyzer.AnalyzerDesign
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.database.views.changes.ChangeDocumentView
import kpn.core.database.views.changes.ChangesView
import kpn.core.database.views.location.LocationView
import kpn.core.database.views.metrics.BackendMetricsView
import kpn.core.database.views.metrics.FrontendMetricsView
import kpn.core.database.views.node.NodeRouteView
import kpn.core.database.views.planner.GraphEdgesView
import kpn.core.database.views.poi.PoiView
import kpn.core.database.views.tile.TileView
import kpn.core.util.Log
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException

/**
 * Forces indexing of Couchdb views. Couchdb indexes are updated upon querying a view. We wait
 * until the view result is received as an indication that indexing is complete. We issue
 * the query again if no result is received within the timeout period.
 */
@Component
class DatabaseIndexer(
  analysisDatabase: Database,
  changeDatabase: Database,
  poiDatabase: Database,
  backendActionsDatabase: Database,
  frontendActionsDatabase: Database
) {

  private val log = Log(classOf[DatabaseIndexer])

  @PostConstruct
  def firstIndexing(): Unit = {
    index(verbose = true)
  }

  @Scheduled(initialDelay = 60000, fixedDelay = 60000)
  def repeatIndexing(): Unit = {
    log.info("start")
    val start = System.currentTimeMillis()
    index(verbose = false)
    val end = System.currentTimeMillis()
    log.info(s"done (${end - start}ms)")
  }

  def index(verbose: Boolean): Unit = {

    indexDatabase(verbose, "analysis-database/analyzer-design", analysisAnalyzerQuery)
    indexDatabase(verbose, "analysis-database/location-design", analysisLocationQuery)
    indexDatabase(verbose, "analysis-database/node-route-design", analysisNodeRouteQuery)
    indexDatabase(verbose, "analysis-database/planner-design", analysisPlannerQuery)
    indexDatabase(verbose, "analysis-database/tile-design", analysisTileQuery)

    if (changeDatabase != null) {
      indexDatabase(verbose, "changes/changes", changeDatabaseQuery)
      indexDatabase(verbose, "changes/documents", changeDocumentsQuery)
    }

    if (poiDatabase != null) {
      indexDatabase(verbose, "poi-database", poiDatabaseQuery)
    }

    if (backendActionsDatabase != null) {
      indexDatabase(verbose, "backend-actions-database", backendActionsDatabaseQuery)
    }

    if (frontendActionsDatabase != null) {
      indexDatabase(verbose, "frontend-actions-database", frontendActionsDatabaseQuery)
    }
  }

  private def analysisAnalyzerQuery(): Unit = {
    DocumentView.counts(analysisDatabase, AnalyzerDesign)
  }

  private def analysisLocationQuery(): Unit = {
    LocationView.query(analysisDatabase, "", NetworkType.canoe, "", stale = false)
  }

  private def analysisNodeRouteQuery(): Unit = {
    NodeRouteView.query(analysisDatabase, NetworkType.canoe, stale = false)
  }

  private def analysisPlannerQuery(): Unit = {
    GraphEdgesView.query(analysisDatabase, NetworkType.canoe, stale = false)
  }

  private def analysisTileQuery(): Unit = {
    TileView.query(analysisDatabase, "tilename", "node", stale = false)
  }

  private def changeDatabaseQuery(): Unit = {
    ChangesView.queryChangeCount(changeDatabase, "node", 0, stale = false) // Long
  }

  private def changeDocumentsQuery(): Unit = {
    ChangeDocumentView.allIds(changeDatabase, "dummy")
  }

  private def poiDatabaseQuery(): Unit = {
    PoiView.query(poiDatabase, 1, 0, stale = false) // PoiViewResult
  }

  private def backendActionsDatabaseQuery(): Unit = {
    val parameters = PeriodParameters("year", 2000, None, None, None, None)
    BackendMetricsView.query(backendActionsDatabase, parameters, "action", average = false, stale = false)
  }

  private def frontendActionsDatabaseQuery(): Unit = {
    val parameters = PeriodParameters("year", 2000, None, None, None, None)
    FrontendMetricsView.query(frontendActionsDatabase, parameters, "action", average = false, stale = false)
  }

  private def indexDatabase(verbose: Boolean, databaseName: String, databaseQuery: () => Unit): Unit = {

    if (verbose) {
      log.info(s"$databaseName - Start waiting for indexing to finish")
    }

    val start = System.currentTimeMillis()
    val timeoutMinutes = 120
    val maxEnd = start + (timeoutMinutes * 60 * 1000)

    var retry = true

    while (retry) {
      try {
        databaseQuery()
        val end = System.currentTimeMillis()
        if (verbose) {
          log.info(s"$databaseName database - indexing completed in ${end - start}ms")
        }
        retry = false
      }
      catch {
        case e: HttpServerErrorException =>
          val now = System.currentTimeMillis()
          if (now > maxEnd) {
            throw new RuntimeException(s"$databaseName database - Maximum wait time ($timeoutMinutes mins) expired")
          }
          if (e.getResponseBodyAsString.contains(s"$databaseName database - The request could not be processed in a reasonable amount of time")) {
            if (verbose) {
              log.info(s"$databaseName database - Continue waiting for indexing to finish")
            }
          }
          else {
            if (verbose) {
              log.info(s"$databaseName database - Continue waiting for indexing to finish (Exception) " + e.getMessage)
            }
          }
      }
    }
  }
}
