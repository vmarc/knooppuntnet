package kpn.core.tools

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.couch.DesignDoc
import kpn.core.db.couch.ViewDoc
import kpn.core.db.json.JsonFormats.designDocFormat
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.ChangesDesign
import kpn.core.db.views.Design
import kpn.core.util.Util

/*
 * Saves our couchdb view definitions in the database.
 */
object DatabaseViewTool {

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: DatabaseViewTool host masterDbName changesDbName")
      System.exit(-1)
    }
    val host = args(0)
    val masterDbName = args(1)
    val changesDbName = args(2)

    Couch.executeIn(host, masterDbName) { database =>
      val design: Design = AnalyzerDesign
      updateView(database, design)
    }

    Couch.executeIn(host, changesDbName) { database =>
      val design: Design = ChangesDesign
      updateView(database, design)
    }

    println("Ready")
  }

  private def updateView(database: Database, design: Design): Unit = {
    val views = design.views.map(v => v.name -> ViewDoc(v.map, v.reduce)).toMap
    val id = "_design/" + Util.classNameOf(design)
    val rev = database.currentRevision(id, Couch.batchTimeout)
    val designDoc = DesignDoc(id, rev, "javascript", views)
    database.authorizedSsave(id, designDocFormat.write(designDoc))
  }
}
