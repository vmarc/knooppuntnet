package kpn.core.database.views.location

import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.shared.NetworkType
import kpn.shared.common.Ref

object LocationView extends View {

  private case class ViewResultRow(value: Seq[String])

  private case class ViewResult(rows: Seq[ViewResultRow])

  def query(database: Database, elementType: String, networkType: NetworkType, locationNames: Seq[String] = Seq(), stale: Boolean = true): Seq[Ref] = {

    val args = Seq(elementType, networkType.name) ++ locationNames

    val query = Query(LocationDesign, LocationView, classOf[ViewResult])
      .keyStartsWith(args: _*)
      .reduce(false)
      .stale(stale)

    val result = database.execute(query)
    result.rows.map { row =>
      val fields = Fields(row.value)
      Ref(
        id = fields.long(1),
        name = fields.string(0)
      )
    }
  }

  override val reduce: Option[String] = Some("_count")
}
