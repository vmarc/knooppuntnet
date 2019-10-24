package kpn.core.tools

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.couch.DesignDoc
import kpn.core.db.couch.ViewDoc
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.ChangesDesign
import kpn.core.db.views.Design
import kpn.core.db.views.LocationDesign
import kpn.core.db.views.PlannerDesign
import kpn.core.db.views.PoiDesign
import kpn.core.util.Util

/*
 * Saves our couchdb view definitions in the database.
 */
object DatabaseViewTool {

  def main(args: Array[String]): Unit = {
    if (args.length < 3) {
      println("Usage: DatabaseViewTool host masterDbName changesDbName poisDbName")
      System.exit(-1)
    }
    val host = args(0)
    val masterDbName = args(1)
    val changesDbName = args(2)
    val poisDbName = args(3)

    Couch.executeIn(host, masterDbName) { database =>
      updateView(database, AnalyzerDesign)
      updateView(database, PlannerDesign)
      updateView(database, LocationDesign)
    }

    Couch.executeIn(host, changesDbName) { database =>
      updateView(database, ChangesDesign)
    }

    Couch.executeIn(host, poisDbName) { database =>
      updateView(database, PoiDesign)
    }
    println("Ready")
  }

  private def updateView(database: Database, design: Design): Unit = {
    val views = design.views.map(v => v.name -> ViewDoc(v.map, v.reduce)).toMap
    val id = "_design/" + Util.classNameOf(design)
    val rev = database.revision(id)
    val designDoc = DesignDoc(id, rev, "javascript", views)
    database.save(designDoc)
  }
}
