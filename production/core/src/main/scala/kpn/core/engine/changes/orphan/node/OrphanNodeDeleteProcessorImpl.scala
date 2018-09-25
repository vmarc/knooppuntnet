package kpn.core.engine.changes.orphan.node

import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.ignore.IgnoredNodeAnalyzer
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.NodeInfoBuilder.fromLoadedNode
import kpn.core.repository.NodeInfoBuilder.fromRawNode
import kpn.core.util.Log
import kpn.shared.Fact
import kpn.shared.LatLonImpl
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.diff.common.FactDiffs

class OrphanNodeDeleteProcessorImpl(
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository,
  ignoredNodeAnalyzer: IgnoredNodeAnalyzer,
  countryAnalyzer: CountryAnalyzer
) extends OrphanNodeDeleteProcessor {

  private val log = Log(classOf[OrphanNodeDeleteProcessorImpl])

  override def process(context: ChangeSetContext, loadedNodeDelete: LoadedNodeDelete): Option[NodeChange] = {

    analysisData.orphanNodes.watched.delete(loadedNodeDelete.id)
    analysisData.orphanNodes.ignored.delete(loadedNodeDelete.id)

    loadedNodeDelete.loadedNode match {
      case Some(loadedNode) =>

        val ignoreFacts = ignoredNodeAnalyzer.analyze(loadedNode)

        val nodeInfo = fromLoadedNode(loadedNode, active = false, ignored = ignoreFacts.nonEmpty, orphan = true, facts = Seq(Fact.Deleted) ++ ignoreFacts)
        analysisRepository.saveNode(nodeInfo)

        if (ignoreFacts.isEmpty) {

          val subsets = loadedNode.subsets

          if (subsets.nonEmpty) {
            Some(
              NodeChange(
                key = context.buildChangeKey(loadedNodeDelete.id),
                changeType = ChangeType.Delete,
                subsets = subsets,
                name = loadedNodeDelete.loadedNode.map(_.name).getOrElse(""),
                before = loadedNodeDelete.loadedNode.map(_.node.raw),
                after = None,
                roleConnectionChanges = Seq(),
                definedInNetworkChanges = Seq(),
                tagDiffs = None,
                nodeMoved = None,
                addedToRoute = Seq(),
                removedFromRoute = Seq(),
                addedToNetwork = Seq(),
                removedFromNetwork = Seq(),
                factDiffs = FactDiffs(),
                Seq(Fact.WasOrphan, Fact.Deleted)
              )
            )
          }
          else {
            None
          }
        }
        else {
          None
        }

      case None =>

        log.warn(s"Could not load the 'before' situation at ${context.timestampBefore.yyyymmddhhmmss} while processing node ${loadedNodeDelete.id} delete" +
          " this is unexpected, please investigate")

        val countryOption = countryAnalyzer.country(Seq(LatLonImpl(loadedNodeDelete.rawNode.latitude, loadedNodeDelete.rawNode.longitude)))
        countryOption match {
          case None => None
          case Some(country) =>

            val nodeInfo = fromRawNode(
              loadedNodeDelete.rawNode,
              country = Some(country),
              active = false,
              orphan = true,
              facts = Seq(Fact.Deleted)
            )
            analysisRepository.saveNode(nodeInfo)

            None
        }
    }
  }
}
