package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.data.MemberType
import kpn.core.doc.RouteRelation
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class RouteIdsAnalyzer(routeRepository: RouteRepository) extends RouteAnalyzer {

  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val subRelationIds = context.route.subRelationTree.toSeq.flatMap(RouteRelation.relationIds).toSet
    val memberRelationIds = context.route.base.members.filter(_.memberType == MemberType.Relation).map(_.id).toSet
    val relationIds = (subRelationIds ++ memberRelationIds ++ Set(context.route._id)).toSeq.sorted
    val routeIds = routeRepository.routeActiveIds(relationIds)
    val unexpectedRelationIds = relationIds.diff(routeIds)
    val facts = if (unexpectedRelationIds.nonEmpty) Seq(Fact.RouteUnexpectedRelation) else Seq.empty
    context.copy(
      _unexpectedRelationIds = Some(unexpectedRelationIds),
      facts = context.facts ++ facts,
      _routeIds = Some(routeIds)
    )
  }
}
