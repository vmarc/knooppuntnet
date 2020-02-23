package kpn.core.database.views.location

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

  def query(database: Database, networkType: NetworkType, nodeId: Long, stale: Boolean): Seq[Ref] = {
    val query = Query(LocationDesign, NodeRouteReferenceView, classOf[ViewResult]).stale(stale).keyStartsWith(networkType.name, nodeId)
    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      Ref(
        key.long(3),
        key.string(2)
      )
    }
  }

  override def reduce: Option[String] = None

}
