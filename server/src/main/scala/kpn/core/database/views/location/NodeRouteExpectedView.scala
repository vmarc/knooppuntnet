package kpn.core.database.views.location

import kpn.api.common.common.NodeRouteCount
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object NodeRouteExpectedView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    key: Seq[String],
    value: Long
  )

  def query(database: Database, networkType: NetworkType, stale: Boolean): Seq[NodeRouteCount] = {

    val query = Query(LocationDesign, NodeRouteExpectedView, classOf[ViewResult])
      .stale(stale)
      .reduce(false)
      .keyStartsWith(networkType.name)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      NodeRouteCount(
        key.long(1),
        row.value.toInt
      )
    }
  }

  override def reduce: Option[String] = None

}
