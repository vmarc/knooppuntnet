package kpn.core.database.views.analyzer

import kpn.api.common.common.Reference
import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object NodeNetworkReferenceView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    value: Option[Reference]
  )

  def query(database: Database, nodeId: Long, stale: Boolean): Seq[Reference] = {
    val query = Query(AnalyzerDesign, NodeNetworkReferenceView, classOf[ViewResult])
      .keyStartsWith(nodeId)
      .stale(stale)
    val result = database.execute(query)
    result.rows.flatMap(_.value)
  }

  override def reduce: Option[String] = None

}
