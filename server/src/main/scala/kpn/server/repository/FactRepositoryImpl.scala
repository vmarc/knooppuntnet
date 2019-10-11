package kpn.server.repository

import akka.util.Timeout
import kpn.core.app.IntegrityCheckPage
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.FactView
import kpn.core.db.views.FactsPerNetworkView
import kpn.shared.Fact
import kpn.shared.RoutesFact
import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.subset.NetworkFactRefs
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class FactRepositoryImpl(mainDatabase: Database) extends FactRepository {

  override def factsPerNetwork(subset: Subset, fact: Fact, timeout: Timeout, stale: Boolean): Seq[NetworkFactRefs] = {

    val rows = mainDatabase.query(AnalyzerDesign, FactsPerNetworkView, timeout, stale)(subset.country.domain, subset.networkType.name, fact.name).map(FactsPerNetworkView.convert)

    @tailrec
    def process(rows: Seq[FactsPerNetworkView.Row], all: Seq[NetworkFactRefs] = Seq.empty): Seq[NetworkFactRefs] = {
      if (rows.isEmpty) {
        all
      }
      else {
        val head = rows.head
        val headRows = rows.takeWhile(_.networkId == head.networkId)
        val tail = rows.drop(headRows.size)
        val refs = headRows.flatMap { row =>
          row.referrerName.map(name => Ref(row.referrerId.get, name))
        }
        val allFacts = all :+ NetworkFactRefs(head.networkId, head.networkName, refs)
        process(tail, allFacts)
      }
    }

    process(rows)
  }

  override def integrityCheckFacts(country: String, networkType: String, timeout: Timeout, stale: Boolean): IntegrityCheckPage = {
    val facts = mainDatabase.query(AnalyzerDesign, FactView, timeout, stale)(country, networkType, "integrityCheckFailed").map(FactView.integrityCheckConvert)
    IntegrityCheckPage(country, networkType, facts)
  }

}
