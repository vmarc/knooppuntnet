package kpn.server.analyzer.full.analyzers

import kpn.api.common.ChangeType
import kpn.api.common.ElementChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.data.MetaData
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.RouteNodeChange
import kpn.core.analysis.Facts
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.ChangeSetRepository
import org.springframework.stereotype.Component

@Component
class InitialRouteChangeBuilder(
  changeSetRepository: ChangeSetRepository
) {

  def saveRouteChange(changeSetContext: ChangeSetContext, routeDoc: RouteDoc): Unit = {

    val key = changeSetContext.buildChangeKey(routeDoc._id)
    val facts = routeDoc.facts
    val locationFacts = facts.filter(Facts.locationFacts.contains)
    val routeData = RouteData(
      routeDoc._id,
      MetaData(
        routeDoc.base.version,
        routeDoc.base.lastUpdated,
        routeDoc.base.changeSetId
      ),
      routeDoc.base.summary.countries.toSeq,
      routeDoc.base.summary.routeTypes,
      routeDoc.base.summary.name: String,
      routeDoc.base.nodes.nodes,
      routeDoc.facts,
      routeDoc.base.summary.meters,
      routeDoc.base.locationAnalysis,
      routeDoc.base.summary.tags
    )

    val nodeChanges = routeDoc.base.nodes.nodes.map { node =>
      RouteNodeChange(
        node.nodeId,
        node.latitude,
        node.longitude,
        ElementChangeType.Added
      )
    }

    changeSetRepository.saveRouteChange(
      RouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.InitialValue,
        name = routeDoc.base.summary.name,
        locationAnalysis = routeDoc.base.locationAnalysis,
        addedToNetwork = Seq.empty,
        removedFromNetwork = Seq.empty,
        before = None,
        after = Some(routeData),
        diffs = RouteDiff(factDiffs = Some(FactDiffs(remaining = facts))),
        nodeChanges = nodeChanges,
        facts = routeDoc.facts,
        investigate = facts.nonEmpty,
        impact = true,
        locationInvestigate = locationFacts.nonEmpty,
        locationImpact = true,
      )
    )
  }
}
