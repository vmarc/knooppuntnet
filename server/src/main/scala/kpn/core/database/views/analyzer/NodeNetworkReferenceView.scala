package kpn.core.database.views.analyzer

import kpn.core.database.Database
import kpn.core.database.views.common.View
import kpn.shared.node.NodeNetworkReference

object NodeNetworkReferenceView extends View {

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    value: Option[NodeNetworkReference]
  )

  def query(database: Database, nodeId: Long, stale: Boolean): Seq[NodeNetworkReference] = {
    val result = database.query(AnalyzerDesign, NodeNetworkReferenceView, classOf[ViewResult], stale = stale)(nodeId)
    result.rows.flatMap(_.value)
  }

  override def reduce: Option[String] = None

}
