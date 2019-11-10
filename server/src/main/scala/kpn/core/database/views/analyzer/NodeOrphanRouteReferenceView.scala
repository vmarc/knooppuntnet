package kpn.core.database.views.analyzer

import kpn.api.common.node.NodeOrphanRouteReference
import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object NodeOrphanRouteReferenceView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    value: Option[NodeOrphanRouteReference]
  )

  def query(database: Database, nodeId: Long, stale: Boolean): Seq[NodeOrphanRouteReference] = {
    val query = Query(AnalyzerDesign, NodeOrphanRouteReferenceView, classOf[ViewResult]).stale(stale).keyStartsWith(nodeId)
    val result = database.execute(query)
    result.rows.flatMap(_.value)
  }

  override def reduce: Option[String] = None

}
