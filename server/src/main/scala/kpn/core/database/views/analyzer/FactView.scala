package kpn.core.database.views.analyzer

import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object FactView extends View {

  case class FactViewKey(
    country: String,
    networkType: String,
    fact: String,
    networkName: String,
    networkId: Long
  )

  private case class ViewResultRow(key: Seq[String])

  private case class ViewResult(rows: Seq[ViewResultRow])

  def query(database: Database, stale: Boolean = true): Seq[FactViewKey] = {
    val query = Query(AnalyzerDesign, FactView, classOf[ViewResult]).stale(stale).reduce(false)
    val result = database.execute(query)
    result.rows.map { row =>
      val fields = Fields(row.key)
      FactViewKey(
        country = fields.string(0),
        networkType = fields.string(1),
        fact = fields.string(2),
        networkName = fields.string(3),
        networkId = fields.long(4)
      )
    }
  }

}
