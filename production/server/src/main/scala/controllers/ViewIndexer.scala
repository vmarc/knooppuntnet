package controllers

import akka.actor.ActorSystem
import kpn.core.db.couch.Database
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.ChangesDesign
import kpn.core.db.views.ChangesView
import kpn.core.db.views.Design
import kpn.core.db.views.DocumentView
import kpn.core.util.Log

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class ViewIndexer(system: ActorSystem, analysisDatabase: Database, changesDatabase: Database) {

  private val log = Log(classOf[ViewIndexer])
  private implicit val executionContext: ExecutionContext = system.dispatcher

  system.scheduler.schedule(50.seconds, 50.seconds) {
    index()
  }

  private def index(): Unit = {
    val start = System.currentTimeMillis()
    index(analysisDatabase, AnalyzerDesign)
    indexPlannerDesign(analysisDatabase)
    index2(changesDatabase, ChangesDesign)
    val end = System.currentTimeMillis()
    log.info(s"Indexing completed in ${end - start}ms")
  }

  private def index(database: Database, design: Design): Unit = {
    try {
      val documentCounts = database.groupQuery(1, design, DocumentView, 20.seconds, stale = false)().map(DocumentView.convert)
      val counts = documentCounts.map(dc => "    '" + dc.prefix + "'=" + dc.count).mkString("\n")
    }
    catch {
      case e: Exception =>
        log.info(s"Exception while indexing ${design.name}: ${e.getMessage}")
    }
  }

  private def index2(database: Database, design: Design): Unit = {
    try {
      database.groupQuery(1, design, ChangesView, 20.seconds, stale = false)()
    }
    catch {
      case e: Exception =>
        log.info(s"Exception while indexing ${design.name}: ${e.getMessage}")
    }
  }

  private def indexPlannerDesign(database: Database): Unit = {
    try {
      // TODO query GraphEdgesView stale=false
    }
    catch {
      case e: Exception =>
        log.info(s"Exception while indexing PlannerDesign: ${e.getMessage}")
    }
  }
}
