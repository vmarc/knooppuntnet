package kpn.core.database.views.changes

import kpn.api.common.changes.filter.ChangesFilterPeriod
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object ChangesView extends View {

  private case class ViewResultRow(key: Seq[String], value: Seq[Long])

  private case class ViewResult(rows: Seq[ViewResultRow])

  def queryPeriod(database: Database, suffixLength: Int, keys: Seq[String]): Seq[ChangesFilterPeriod] = {

    def keyString(values: Seq[String]): String = values.mkString("[\"", "\", \"", "\"]")

    def suffix(size: Int, character: String): String = Seq.fill(size)(character).mkString

    val startKey = keyString(keys :+ suffix(suffixLength, "9"))
    val endKey = keyString(keys :+ suffix(suffixLength, "0"))

    val query = Query(ChangesDesign, ChangesView, classOf[ViewResult])
      .startKey(startKey)
      .endKey(endKey)
      .reduce(true)
      .descending(true)
      .groupLevel(keys.size + 1)

    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      ChangesFilterPeriod(
        name = key.string(keys.size),
        totalCount = row.value.head,
        impactedCount = row.value(1)
      )
    }
  }

  def queryChangeCount(database: Database, elementType: String, elementId: Long): Long = {
    val query = Query(ChangesDesign, ChangesView, classOf[ViewResult])
      .keyStartsWith(elementType, elementId.toString)
      .groupLevel(2)
    val result = database.execute(query)
    result.rows.map(_.value.head).sum
  }

  override val reduce: Option[String] = Some("_sum")
}
