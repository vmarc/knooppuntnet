package kpn.server.analyzer.full.analyzers

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.data.MetaData
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
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

    val key = changeSetContext.buildChangeKey(routeDoc.id)
    val facts = routeDoc.facts
    val locationFacts = facts.filter(Facts.locationFacts.contains)
    val routeData = RouteData(
      routeDoc.summary.id,
      MetaData(routeDoc.version, routeDoc.lastUpdated, routeDoc.changeSetId),
      routeDoc.summary.countries.toSeq,
      routeDoc.summary.routeTypes,
      routeDoc.summary.name: String,
      routeDoc.nodes.nodes,
      Seq.empty, // TODO redesign - ways ???
      routeDoc.facts,
      routeDoc.summary.meters,
      routeDoc.locationAnalysis,
      routeDoc.summary.tags
    )

    changeSetRepository.saveRouteChange(
      RouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.InitialValue,
        name = routeDoc.summary.name,
        locationAnalysis = routeDoc.locationAnalysis,
        addedToNetwork = Seq.empty,
        removedFromNetwork = Seq.empty,
        before = None,
        after = Some(routeData),
        removedWays = Seq.empty,
        addedWays = Seq.empty,
        updatedWays = Seq.empty,
        diffs = RouteDiff(factDiffs = Some(FactDiffs(remaining = facts))),
        facts = routeDoc.facts,
        investigate = facts.nonEmpty,
        impact = true,
        locationInvestigate = locationFacts.nonEmpty,
        locationImpact = true,
      )
    )
  }
}
