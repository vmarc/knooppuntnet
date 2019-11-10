package kpn.core.database.views.analyzer

import kpn.api.common.network.NetworkMapInfo
import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object NetworkMapView extends View {

  private case class ViewResultRow(value: NetworkMapInfo)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def query(database: Database, subset: Subset, stale: Boolean = true): Seq[NetworkMapInfo] = {
    val query = Query(AnalyzerDesign, NetworkView, classOf[ViewResult])
      .keyStartsWith(subset.country.domain, subset.networkType.name)
      .reduce(false)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map(_.value)
  }

  override val reduce: Option[String] = None
}
