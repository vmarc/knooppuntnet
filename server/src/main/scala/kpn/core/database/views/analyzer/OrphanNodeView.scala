package kpn.core.database.views.analyzer

import kpn.core.database.Database
import kpn.core.database.views.common.View
import kpn.shared.NodeInfo
import kpn.shared.Subset

object OrphanNodeView extends View {

  private case class ViewResultRow(
    value: NodeInfo
  )

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  def query(database: Database, subset: Subset, stale: Boolean = true): Seq[NodeInfo] = {
    val country = subset.country.domain
    val networkType = subset.networkType.name
    val result = database.query(AnalyzerDesign, OrphanNodeView, classOf[ViewResult], stale = stale)(country, networkType)
    result.rows.map(_.value)
  }

  override val reduce: Option[String] = Some("_count")
}
