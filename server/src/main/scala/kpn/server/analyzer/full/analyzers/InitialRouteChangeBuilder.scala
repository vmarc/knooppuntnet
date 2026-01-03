package kpn.server.analyzer.full.analyzers

import kpn.api.common.ChangeType
import kpn.api.common.ElementChangeType
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.RouteChange
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
      routeDoc.base.raw,
      routeDoc.base.countries.toSeq,
      routeDoc.base.routeTypes,
      routeDoc.base.name: String,
      routeDoc.base.nodes.nodes,
      routeDoc.facts,
      routeDoc.base.meters,
      routeDoc.base.locationAnalysis
    )

    val nodeChanges = routeDoc.base.nodes.nodes.map { node =>
      RouteNodeChange(
        node.nodeId,
        node.latitude,
        node.longitude,
        ElementChangeType.Added
      )
    }

    changeSetRepository.saveBaseRouteChange(
      BaseRouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.InitialValue,
        before = None,
        after = Some(routeDoc.base.raw.meta),
        routeDiff = RouteDiff(
          nameDiff = None,
          roleDiff = None,
          factDiffs = Some(FactDiffs(remaining = facts)),
          nodeDiffs = Seq.empty,
          memberOrderChanged = false,
          tagDiffs = None
        ),
        wayDiffs = None,
        geometryDiff = None
      )
    )

    changeSetRepository.saveRouteChange(
      RouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.InitialValue,
        name = routeDoc.base.name,
        locationAnalysis = routeDoc.base.locationAnalysis,
        addedToNetwork = Seq.empty,
        removedFromNetwork = Seq.empty,
        before = None,
        after = Some(routeData),
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
