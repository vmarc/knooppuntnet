package kpn.core.database.views.action

import kpn.api.common.status.NameValue
import kpn.api.custom.Timestamp
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object BackendActionView extends View {

  private case class ViewResultRow(key: Seq[String], value: Seq[Long])

  private case class ViewResult(rows: Seq[ViewResultRow])

  override def reduce: Option[String] = sumAndCount

  def queryDay(database: Database, timestamp: Timestamp, action: String, stale: Boolean = true): Seq[NameValue] = {

    val query = Query(BackendActionDesign, BackendActionView, classOf[ViewResult])
      .keyStartsWith("time", action, timestamp.year, timestamp.month, timestamp.day)
      .reduce(true)
      .groupLevel(6)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      val hour = f"${key.int(5)}%02d"
      val count = row.value.head
      NameValue(hour, count)
    }
  }

  def queryDayAverage(database: Database, timestamp: Timestamp, action: String, stale: Boolean = true): Seq[NameValue] = {

    val query = Query(BackendActionDesign, BackendActionView, classOf[ViewResult])
      .keyStartsWith("time", action, timestamp.year, timestamp.month, timestamp.day)
      .reduce(true)
      .groupLevel(6)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      val hour = f"${key.int(5)}%02d"
      val sum = row.value.head
      val count = row.value(1)
      NameValue(hour, sum / count)
    }
  }

}
