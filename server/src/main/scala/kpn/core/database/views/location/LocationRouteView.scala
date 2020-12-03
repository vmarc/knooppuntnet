package kpn.core.database.views.location

import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Day
import kpn.api.custom.LocationKey
import kpn.api.custom.Timestamp
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object LocationRouteView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    key: Seq[String],
    value: ViewResultRowValue
  )

  private case class ViewResultRowValue(
    meters: Long,
    lastUpdated: Timestamp,
    lastSurvey: Option[Day],
    broken: Boolean,
    accessible: Boolean
  )

  private case class CountViewResult(
    rows: Seq[CountViewResultRow]
  )

  private case class CountViewResultRow(
    value: Long
  )

  def query(database: Database, locationKey: LocationKey, parameters: LocationRoutesParameters, stale: Boolean): Seq[LocationRouteInfo] = {

    val skip = parameters.itemsPerPage * parameters.pageIndex
    val limit = parameters.itemsPerPage

    val query = Query(LocationDesign, LocationRouteView, classOf[ViewResult])
      .stale(stale)
      .keyStartsWith(locationKey.networkType.name, locationKey.name)
      .reduce(false)
      .skip(skip)
      .limit(limit)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      LocationRouteInfo(
        id = key.long(3),
        name = key.string(2),
        meters = row.value.meters,
        lastUpdated = row.value.lastUpdated,
        lastSurvey = row.value.lastSurvey,
        broken = row.value.broken,
        accessible = row.value.accessible
      )
    }
  }

  def queryCount(database: Database, locationKey: LocationKey, stale: Boolean): Long = {

    val query = Query(LocationDesign, LocationRouteView, classOf[CountViewResult])
      .stale(stale)
      .keyStartsWith(locationKey.networkType.name, locationKey.name)
      .reduce(true)
      .groupLevel(2)

    val result = database.execute(query)
    result.rows.headOption match {
      case Some(row) => row.value
      case None => 0
    }
  }

  override def reduce: Option[String] = Some("_count")
}
