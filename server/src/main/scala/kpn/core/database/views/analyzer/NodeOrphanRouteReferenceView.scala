package kpn.core.database.views.analyzer

import kpn.core.database.Database
import kpn.core.database.views.common.View
import kpn.shared.node.NodeOrphanRouteReference

object NodeOrphanRouteReferenceView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    value: Option[NodeOrphanRouteReference]
  )

  def query(database: Database, nodeId: Long, stale: Boolean): Seq[NodeOrphanRouteReference] = {
    val result = database.query(AnalyzerDesign, NodeOrphanRouteReferenceView, classOf[ViewResult], stale = stale)(nodeId)
    result.rows.flatMap(_.value)
  }

  override def reduce: Option[String] = None

}
