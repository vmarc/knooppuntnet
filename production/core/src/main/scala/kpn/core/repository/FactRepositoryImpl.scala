package kpn.core.repository

import akka.util.Timeout
import kpn.core.app.IntegrityCheckPage
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.FactView
import kpn.core.db.views.RouteFactView
import kpn.shared.Fact
import kpn.shared.RoutesFact
import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.subset.NetworkRoutesFacts

import scala.annotation.tailrec

class FactRepositoryImpl(database: Database) extends FactRepository {

  override def routeFacts(subset: Subset, fact: Fact, timeout: Timeout, stale: Boolean): Seq[NetworkRoutesFacts] = {

    val rows = database.query(AnalyzerDesign, RouteFactView, timeout, stale)(subset.country.domain, subset.networkType.name, fact.name).map(RouteFactView.convert)

    @tailrec
    def process(rows: Seq[RouteFactView.Row], all: Seq[NetworkRoutesFacts] = Seq()): Seq[NetworkRoutesFacts] = {
      if (rows.isEmpty) {
        all
      }
      else {
        val head = rows.head
        val headRows = rows.takeWhile(_.networkId == head.networkId)
        val tail = rows.drop(headRows.size)
        val refs = headRows.flatMap { row =>
          row.routeName.map(name => Ref(row.routeId.get, name))
        }
        val allFacts = all :+ NetworkRoutesFacts(head.networkId, head.networkName, RoutesFact(refs))
        process(tail, allFacts)
      }
    }

    process(rows)
  }

  override def integrityCheckFacts(country: String, networkType: String, timeout: Timeout, stale: Boolean): IntegrityCheckPage = {
    val facts = database.query(AnalyzerDesign, FactView, timeout, stale)(country, networkType, "integrityCheckFailed").map(FactView.integrityCheckConvert)
    IntegrityCheckPage(country, networkType, facts)
  }

  override def networkCollections(): Seq[Long] = {
    val rows = database.query(AnalyzerDesign, FactView, Couch.batchTimeout)().map(FactView.convert)
    rows.flatMap { row =>
      if (row.fact == Fact.IgnoreNetworkCollection.name) {
        Some(row.networkId)
      }
      else {
        None
      }
    }
  }
}
