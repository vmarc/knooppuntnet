package kpn.core.database.views.action

import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.Design
import kpn.core.database.views.common.View

object LastKnownActionQuery {
  private case class ViewResultRow(value: Long)
  private case class ViewResult(rows: Seq[ViewResultRow])
}

class LastKnownActionQuery(database: Database, design: Design, view: View, action: String, stale: Boolean = true) {

  import LastKnownActionQuery._

  def value(): Long = {

    val query = Query(design, view, classOf[ViewResult])
      .startKey(s"""["time", "$action",{}]""")
      .endKey(s"""["time", "$action"]""")
      .reduce(false)
      .descending(true)
      .limit(1)
      .stale(stale)
    val result = database.execute(query)
    if (result.rows.isEmpty) {
      0
    }
    else {
      result.rows.head.value
    }
  }

}
