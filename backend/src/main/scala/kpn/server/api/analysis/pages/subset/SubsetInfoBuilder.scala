package kpn.server.api.analysis.pages.subset

import kpn.api.common.Fact
import kpn.api.common.subset.SubsetInfo
import kpn.api.custom.Subset
import kpn.core.analysis.Facts
import kpn.core.app.stats.Figure

object SubsetInfoBuilder {

  def newSubsetInfo(subset: Subset, figures: Map[String, Figure]): SubsetInfo = {

    val networkCount = figures.get("NetworkCount") match {
      case Some(figure: Figure) => figure.counts.getOrElse(subset, 0L)
      case _ => 0L
    }
    val factCount = Facts.reportedFacts.map { f =>
      figures.get(s"${f.entryName}Count") match {
        case Some(figure: Figure) => figure.counts.getOrElse(subset, 0L)
        case _ => 0L
      }
    }.sum

    val orphanNodeCount = figures.get(s"${Fact.OrphanNode.entryName}Count") match {
      case Some(figure: Figure) => figure.counts.getOrElse(subset, 0L)
      case _ => 0L
    }
    val orphanRouteCount = figures.get(s"${Fact.OrphanRoute.entryName}Count") match {
      case Some(figure: Figure) => figure.counts.getOrElse(subset, 0L)
      case _ => 0L
    }

    SubsetInfo(
      subset.country,
      subset.routeType,
      networkCount,
      factCount,
      0,
      orphanNodeCount,
      orphanRouteCount
    )
  }
}
