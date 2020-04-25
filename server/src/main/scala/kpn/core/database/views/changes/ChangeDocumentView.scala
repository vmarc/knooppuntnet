package kpn.core.database.views.changes

import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object ChangeDocumentView extends View {

  case class ViewResultRow(key: Seq[String])

  case class ViewResult(rows: Seq[ViewResultRow])

  def allNetworkIds(database: Database): Seq[Long] = {
    allIds(database: Database, "network")
  }

  def allRouteIds(database: Database): Seq[Long] = {
    allIds(database: Database, "route")
  }

  def allNodeIds(database: Database): Seq[Long] = {
    allIds(database: Database, "node")
  }

  def allIds(database: Database, elementType: String): Seq[Long] = {
    val query = Query(ChangeDocumentsDesign, ChangeDocumentView, classOf[ViewResult])
      .keyStartsWith(elementType)
      .reduce(true)
      .groupLevel(2)
      .stale(false)
    val result = database.execute(query)
    result.rows.map { row =>
      val fields = Fields(row.key)
      fields.long(1)
    }
  }

  override val reduce: Option[String] = Some("_count")
}
