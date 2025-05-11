package kpn.server.analyzer.engine.changes.route

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc

object RouteTileDocBuilder {

  def build(context: BaseRouteAnalysisContext): Seq[RouteTileDoc] = {
    context.tileDatas.map { tileData =>
      RouteTileDoc(
        _id = s"${tileData.name}-${context.routeId}",
        routeId = context.relation.id,
        routeName = context.routeNameAnalysis.name.getOrElse("no-name"), // TODO redesign tiles - can do better?
        routeTypes = context.routeTypes,
        z = tileData.z,
        x = tileData.x,
        y = tileData.y,
        layer = tileData.layer,
        scope = tileData.scope,
        survey = tileData.survey,
        error = tileData.error,
        segments = tileData.segments
      )
    }
  }
}
