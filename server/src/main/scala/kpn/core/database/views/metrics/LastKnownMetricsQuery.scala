package kpn.core.database.views.metrics

import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object LastKnownMetricsQuery {
  private case class ViewResultRow(value: Long)
  private case class ViewResult(rows: Seq[ViewResultRow])
}

class LastKnownMetricsQuery(database: Database, design: Design, view: View, action: String) {

  import LastKnownMetricsQuery._

  def value(): Long = {

    val query = Query(design, view, classOf[ViewResult])
      .startKey(s"""["time", "$action",{}]""")
      .endKey(s"""["time", "$action"]""")
      .reduce(false)
      .descending(true)
      .limit(1)
    val result = database.execute(query)
    if (result.rows.isEmpty) {
      0
    }
    else {
      result.rows.head.value
    }
  }

}
