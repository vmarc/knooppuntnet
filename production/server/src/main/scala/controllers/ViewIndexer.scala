package controllers

import akka.actor.ActorSystem
import kpn.core.db.couch.Database
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.ChangesDesign
import kpn.core.db.views.Design
import kpn.core.db.views.DocumentView
import kpn.core.util.Log

import scala.concurrent.duration.DurationInt

class ViewIndexer(system: ActorSystem, analysisDatabase: Database, changesDatabase: Database) {

  private val log = Log(classOf[ViewIndexer])

  implicit val executionContext = system.dispatcher

  system.scheduler.schedule(50.seconds, 50.seconds) {
    index()
  }

  private def index(): Unit = {
    val start = System.currentTimeMillis()
    index(analysisDatabase, AnalyzerDesign)
    index(changesDatabase, ChangesDesign)
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
}
