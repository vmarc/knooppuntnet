package kpn.server.analyzer.engine.changes.route.base

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo

object RouteTileInfoBuilder {

  def build(context: BaseRouteAnalysisContext): Seq[RouteTileInfo] = {
    context.tileDatas.map { tileData =>
      RouteTileInfo(
        _id = s"${tileData.name}-${context.routeId}",
        routeId = context.relation.id,
        routeName = context.routeNameAnalysis.name.getOrElse("no-name"),
        routeTypes = context.routeTypes,
        z = tileData.z,
        x = tileData.x,
        y = tileData.y,
        layer = tileData.layer,
        scope = tileData.scope,
        survey = tileData.survey,
        error = tileData.error,
        proposed = tileData.proposed,
        segments = tileData.segments
      )
    }
  }
}
