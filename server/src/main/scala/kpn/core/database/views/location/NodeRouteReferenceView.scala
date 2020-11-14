package kpn.core.database.views.location

import kpn.api.common.common.NodeRouteCount
import kpn.api.common.common.Ref
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object NodeRouteReferenceView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    key: Seq[String]
  )

  private case class CountResult(
    rows: Seq[CountResultRow]
  )

  private case class CountResultRow(
    key: Seq[String],
    value: Long
  )

  def query(database: Database, networkType: NetworkType, nodeId: Long, stale: Boolean): Seq[Ref] = {

    val query = Query(LocationDesign, NodeRouteReferenceView, classOf[ViewResult])
      .stale(stale)
      .reduce(false)
      .keyStartsWith(networkType.name, nodeId)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      Ref(
        key.long(3),
        key.string(2)
      )
    }
  }

  def queryCount(database: Database, networkType: NetworkType, stale: Boolean): Seq[NodeRouteCount] = {

    val query = Query(LocationDesign, NodeRouteReferenceView, classOf[CountResult])
      .stale(stale)
      .keyStartsWith(networkType.name)
      .reduce(true)
      .groupLevel(2)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      NodeRouteCount(
        key.long(1),
        row.value.toInt
      )
    }
  }

  override def reduce: Option[String] = Some("_sum")

}
