package kpn.core.database.views.location

import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.api.custom.Timestamp
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object LocationNodeView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    key: Seq[String],
    value: Seq[String]
  )

  private case class CountViewResult(
    rows: Seq[CountViewResultRow]
  )

  private case class CountViewResultRow(
    value: Int
  )

  private case class CountryViewResult(
    rows: Seq[CountryViewResultRow]
  )

  private case class CountryViewResultRow(
    key: Seq[String],
    value: Int
  )

  def query(database: Database, locationKey: LocationKey, parameters: LocationNodesParameters, stale: Boolean): Seq[LocationNodeInfo] = {

    val skip = parameters.itemsPerPage * parameters.pageIndex
    val limit = parameters.itemsPerPage

    val query = Query(LocationDesign, LocationNodeView, classOf[ViewResult])
      .stale(stale)
      .keyStartsWith(locationKey.networkType.name, locationKey.country.domain, locationKey.name)
      .reduce(false)
      .skip(skip)
      .limit(limit)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      val value = Fields(row.value)

      LocationNodeInfo(
        id = key.long(4),
        name = key.string(3),
        latitude = value.string(0),
        longitude = value.string(1),
        lastUpdated = Timestamp.fromIso(value.string(2)),
        factCount = value.int(3),
        routeReferences = Seq.empty
      )
    }
  }

  def queryCount(database: Database, locationKey: LocationKey, stale: Boolean): Long = {

    val query = Query(LocationDesign, LocationNodeView, classOf[CountViewResult])
      .stale(stale)
      .keyStartsWith(locationKey.networkType.name, locationKey.country.domain, locationKey.name)
      .reduce(true)
      .groupLevel(3)

    val result = database.execute(query)
    if (result.rows.nonEmpty) {
      result.rows.head.value
    }
    else {
      0L
    }
  }


  def countryLocations(database: Database, networkType: NetworkType, country: Country, stale: Boolean): Seq[LocationNodeCount] = {

    val query = Query(LocationDesign, LocationNodeView, classOf[CountryViewResult])
      .stale(stale)
      .keyStartsWith(networkType.name, country.domain)
      .groupLevel(3)
      .reduce(true)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      LocationNodeCount(
        name = key.string(2),
        count = row.value
      )
    }
  }

  override def reduce: Option[String] = Some("_count")
}
