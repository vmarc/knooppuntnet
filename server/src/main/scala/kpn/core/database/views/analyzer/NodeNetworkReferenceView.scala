package kpn.core.database.views.analyzer

import kpn.api.common.node.NodeNetworkReference
import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object NodeNetworkReferenceView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    value: Option[NodeNetworkReference]
  )

  def query(database: Database, nodeId: Long, stale: Boolean): Seq[NodeNetworkReference] = {
    val query = Query(AnalyzerDesign, NodeNetworkReferenceView, classOf[ViewResult])
      .keyStartsWith(nodeId)
      .stale(stale)
    val result = database.execute(query)
    result.rows.flatMap(_.value)
  }

  override def reduce: Option[String] = None

}
