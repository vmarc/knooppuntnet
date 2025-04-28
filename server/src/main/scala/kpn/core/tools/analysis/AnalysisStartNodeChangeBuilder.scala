package kpn.core.tools.analysis

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.Subset
import kpn.core.analysis.Facts
import kpn.core.doc.NodeDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.ChangeSetRepository

class AnalysisStartNodeChangeBuilder(
  changeSetContext: ChangeSetContext,
  changeSetRepository: ChangeSetRepository
) {

  def buildAndSave(nodeDoc: NodeDoc): Unit = {
    val nodeChange = buildNodeChange(nodeDoc)
    changeSetRepository.saveNodeChange(nodeChange)
  }

  private def buildNodeChange(nodeDoc: NodeDoc): NodeChange = {
    val key = changeSetContext.buildChangeKey(nodeDoc._id)
    val facts = nodeDoc.facts
    val locationFacts = facts.filter(Facts.locationFacts.contains)

    NodeChange(
      _id = key.toId,
      key = key,
      changeType = ChangeType.InitialValue,
      subsets = determineSubsets(nodeDoc),
      locations = nodeDoc.locations,
      name = nodeDoc.name,
      before = None,
      after = Some(nodeDoc.toMeta),
      connectionChanges = Seq.empty,
      roleConnectionChanges = Seq.empty,
      definedInNetworkChanges = Seq.empty,
      tagDiffs = None,
      nodeMoved = None,
      addedToRoute = Seq.empty,
      removedFromRoute = Seq.empty,
      addedToNetwork = Seq.empty,
      removedFromNetwork = Seq.empty,
      factDiffs = createFactDiffs(facts),
      facts = Seq.empty,
      initialTags = Some(nodeDoc.tags),
      initialLatLon = Some(LatLonImpl(nodeDoc.latitude, nodeDoc.longitude)),
      investigate = facts.nonEmpty,
      impact = true,
      locationInvestigate = locationFacts.nonEmpty,
      locationImpact = true
    )
  }

  private def determineSubsets(nodeDoc: NodeDoc): Seq[Subset] = {
    nodeDoc.country.map { country =>
      nodeDoc.names.map(_.routeType).distinct.map(routeType => Subset(country, routeType))
    }.getOrElse(Seq.empty)
  }

  private def createFactDiffs(facts: Seq[Fact]): Option[FactDiffs] = {
    if (facts.nonEmpty) Some(FactDiffs(remaining = facts)) else None
  }
}
