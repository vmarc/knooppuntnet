package kpn.core.load

import kpn.core.db.couch.Couch
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.FactView
import kpn.shared.Fact

object TempIgnoreNetworkCollection extends App {
  Couch.executeIn("master") { database =>
    val rows = database.query(AnalyzerDesign, FactView, Couch.uiTimeout, stale = false)().map(FactView.convert)
    rows.foreach { row =>
      if (row.fact == Fact.IgnoreNetworkCollection.name) {
        println(row)
      }
    }
  }
}
