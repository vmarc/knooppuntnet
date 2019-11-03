package kpn.core.database.views.analyzer

import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.shared.Subset
import kpn.shared.network.NetworkAttributes

object NetworkView extends View {

  private case class ViewResultRow(value: NetworkAttributes)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def query(database: Database, subset: Subset, stale: Boolean = true): Seq[NetworkAttributes] = {
    val query = Query(AnalyzerDesign, NetworkView, classOf[ViewResult])
      .keyStartsWith(subset.country.domain, subset.networkType.name)
      .reduce(false)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map(_.value)
  }

  override val reduce: Option[String] = None
}
