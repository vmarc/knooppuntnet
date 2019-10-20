package kpn.core.tools.db

import kpn.core.db.couch.Couch
import kpn.core.db.couch.OldDatabase
import kpn.core.db.json.JsonFormats.changeSetSummaryDocFormat
import kpn.core.db.json.JsonFormats.nodeChangeDocFormat
import kpn.core.db.json.JsonFormats.routeChangeDocFormat
import kpn.core.db.views.ChangesDesign
import kpn.core.db.views.ChangesView
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeAnalyzer
import kpn.shared.ChangeSetSummary
import spray.json.JsString

object PatchChangesTool {
  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("Usage: PatchChangesTool host changesDbName")
      System.exit(-1)
    }
    val host = args(0)
    val changesDbName = args(1)

    Couch.oldExecuteIn(host, changesDbName) { database =>
      new PatchChangesTool(database).run()
    }
  }
}

class PatchChangesTool(database: OldDatabase) {

  def run(): Unit = {
    patchNodeChanges()
    patchRouteChanges()
    patchChangeSetSummaries()
  }

  private def patchNodeChanges(): Unit = {
    println("collecting node changes...")
    val ids = collectChangeDocumentIds("node")
    println(s"processing ${ids.size} node changes")
    var updatedCount = 0
    ids.zipWithIndex.foreach { case (id, index) =>
      database.optionGet(id).foreach { jsValue =>
        val updated = jsValue.asJsObject().getFields("nodeChange").exists { nodeChange =>
          nodeChange.asJsObject().getFields("happy").nonEmpty
        }
        if (!updated) {
          val doc = nodeChangeDocFormat.read(jsValue)
          val analyzed = new NodeChangeAnalyzer(doc.nodeChange).analyzed()
          database.save(id, nodeChangeDocFormat.write(doc.copy(nodeChange = analyzed)))
          updatedCount = updatedCount + 1
        }
      }
      if (((index + 1) % 100) == 0) {
        println(s"${ids.size}/${index + 1}/updated=$updatedCount")
      }
    }

    println(s"done processing ${ids.size} node changes, updated $updatedCount")
  }

  private def patchRouteChanges(): Unit = {
    println("collecting route changes...")
    val ids = collectChangeDocumentIds("route")
    println(s"processing ${ids.size} route changes")
    var updatedCount = 0
    ids.zipWithIndex.foreach { case (id, index) =>
      database.optionGet(id).foreach { jsValue =>
        val updated = jsValue.asJsObject().getFields("routeChange").exists { nodeChange =>
          nodeChange.asJsObject().getFields("happy").nonEmpty
        }
        if (!updated) {
          val doc = routeChangeDocFormat.read(jsValue)
          val analyzed = new RouteChangeAnalyzer(doc.routeChange).analyzed()
          database.save(id, routeChangeDocFormat.write(doc.copy(routeChange = analyzed)))
          updatedCount = updatedCount + 1
        }
      }
      if (((index + 1) % 100) == 0) {
        println(s"${ids.size}/${index + 1}/updated=$updatedCount")
      }
    }
    println(s"done processing ${ids.size} route changes, updated $updatedCount")
  }

  private def patchChangeSetSummaries(): Unit = {
    println("collecting changeset summaries...")
    val ids = collectChangeDocumentIds("change-set")
    println(s"processing ${ids.size} changeset summaries")
    var updatedCount = 0
    ids.zipWithIndex.foreach { case (id, index) =>
      database.optionGet(id).foreach { jsValue =>
        val updated = jsValue.asJsObject().getFields("changeSetSummary").exists { changeSetSummary =>
          changeSetSummary.asJsObject().getFields("subsetAnalyses").nonEmpty
        }
        if (!updated) {
          val doc = changeSetSummaryDocFormat.read(jsValue)
          val analyzed = ChangeSetSummary(
            doc.changeSetSummary.key,
            doc.changeSetSummary.timestampFrom,
            doc.changeSetSummary.timestampUntil,
            doc.changeSetSummary.networkChanges,
            doc.changeSetSummary.orphanRouteChanges,
            doc.changeSetSummary.orphanNodeChanges
          )
          database.save(id, changeSetSummaryDocFormat.write(doc.copy(changeSetSummary = analyzed)))
          updatedCount = updatedCount + 1
        }
      }
      if (((index + 1) % 100) == 0) {
        println(s"${ids.size}/${index + 1}/updated=$updatedCount")
      }
    }
    println(s"done processing ${ids.size} changeset summaries, updated $updatedCount")
  }

  private def collectChangeDocumentIds(elementType: String): Seq[String] = {
    val rows = database.query(ChangesDesign, ChangesView, stale = true)(elementType)
    rows.flatMap(_.asJsObject.getFields("id")).flatMap {
      case string: JsString => Some(string.value)
      case _ => None
    }
  }

}
