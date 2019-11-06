package kpn.core.database.views.analyzer

import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.subset.NetworkFactRefs

import scala.annotation.tailrec

/*
 * Facts per network
 */
object FactsPerNetworkView extends View {

  private case class ViewResultRow(key: Seq[String])

  private case class ViewResult(rows: Seq[ViewResultRow])

  private case class Row(
    networkName: String,
    networkId: Long,
    referrerName: Option[String],
    referrerId: Option[Long]
  )

  def query(database: Database, subset: Subset, fact: Fact, stale: Boolean): Seq[NetworkFactRefs] = {
    val query = Query(AnalyzerDesign, FactsPerNetworkView, classOf[ViewResult])
      .keyStartsWith(subset.country.domain, subset.networkType.newName, fact.name)
      .reduce(false)
      .stale(stale)
    val result = database.execute(query)
    val rows = result.rows.map { row =>
      val fields = Fields(row.key)
      Row(
        networkName = fields.string(3),
        networkId = fields.long(4),
        referrerName = fields.optionalString(5),
        referrerId = fields.optionalLong(6)
      )
    }

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

  override val reduce: Option[String] = Some("_sum")
}
