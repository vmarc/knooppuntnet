package kpn.core.database.views.analyzer

import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Day
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object OrphanRouteView extends View {

  private case class Value(
    meters: Long,
    isBroken: Boolean,
    accessible: Boolean,
    lastSurvey: Option[Day],
    lastUpdated: Timestamp
  )

  private case class ViewResultRow(
    key: Seq[String],
    value: Value
  )

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  def query(database: Database, subset: Subset, stale: Boolean = true): Seq[OrphanRouteInfo] = {
    val country = subset.country.domain
    val networkType = subset.networkType.name
    val query = Query(AnalyzerDesign, OrphanRouteView, classOf[ViewResult])
      .keyStartsWith(country, networkType)
      .reduce(false)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      OrphanRouteInfo(
        id = key.long(3),
        name = key.string(2),
        meters = row.value.meters,
        isBroken = row.value.isBroken,
        accessible = row.value.accessible,
        lastSurvey = row.value.lastSurvey.map(_.yyyymmdd).getOrElse("-"),
        lastUpdated = row.value.lastUpdated
      )
    }
  }

  override val reduce: Option[String] = Some("_count")
}
