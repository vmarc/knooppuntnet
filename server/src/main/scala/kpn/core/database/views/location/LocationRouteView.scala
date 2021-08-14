package kpn.core.database.views.location

import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Day
import kpn.api.custom.LocationKey
import kpn.api.custom.LocationRoutesType
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

  def query(database: Database, locationKey: LocationKey, parameters: LocationRoutesParameters): Seq[LocationRouteInfo] = {

    val skip = parameters.itemsPerPage * parameters.pageIndex
    val limit = parameters.itemsPerPage

    val query = Query(LocationDesign, LocationRouteView, classOf[ViewResult])
      .keyStartsWith(
        locationKey.networkType.name,
        locationKey.name,
        parameters.locationRoutesType.name
      )
      .reduce(false)
      .skip(skip)
      .limit(limit)

    val (idIndex, nameIndex) = if (parameters.locationRoutesType == LocationRoutesType.survey) {
      (5, 4)
    }
    else {
      (4, 3)
    }

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      LocationRouteInfo(
        id = key.long(idIndex),
        name = key.string(nameIndex),
        meters = row.value.meters,
        lastUpdated = row.value.lastUpdated,
        lastSurvey = row.value.lastSurvey,
        broken = row.value.broken,
        accessible = row.value.accessible
      )
    }
  }

  def queryCount(database: Database, locationKey: LocationKey, locationRoutesType: LocationRoutesType): Long = {

    val query = Query(LocationDesign, LocationRouteView, classOf[CountViewResult])
      .keyStartsWith(
        locationKey.networkType.name,
        locationKey.name,
        locationRoutesType.name
      )
      .reduce(true)
      .groupLevel(3)

    val result = database.execute(query)
    result.rows.headOption match {
      case Some(row) => row.value
      case None => 0
    }
  }

  override def reduce: Option[String] = Some("_count")
}
