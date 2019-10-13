package kpn.server.analyzer.engine.changes.orphan.node

import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.NodeInfoBuilder.fromLoadedNode
import kpn.server.repository.NodeInfoBuilder.fromRawNode
import kpn.core.util.Log
import kpn.server.analyzer.engine.AnalysisContext
import kpn.shared.Fact
import kpn.shared.LatLonImpl
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.diff.common.FactDiffs
import org.springframework.stereotype.Component

@Component
class OrphanNodeDeleteProcessorImpl(
  analysisContext: AnalysisContext,
  analysisRepository: AnalysisRepository,
  countryAnalyzer: CountryAnalyzer
) extends OrphanNodeDeleteProcessor {

  private val log = Log(classOf[OrphanNodeDeleteProcessorImpl])

  override def process(context: ChangeSetContext, loadedNodeDelete: LoadedNodeDelete): Option[NodeChange] = {

    analysisContext.data.orphanNodes.watched.delete(loadedNodeDelete.id)

    loadedNodeDelete.loadedNode match {
      case Some(loadedNode) =>

        val nodeInfo = fromLoadedNode(loadedNode, active = false, orphan = true, facts = Seq(Fact.Deleted))
        analysisRepository.saveNode(nodeInfo)

        val subsets = loadedNode.subsets

        if (subsets.nonEmpty) {
          Some(
            analyzed(
              NodeChange(
                key = context.buildChangeKey(loadedNodeDelete.id),
                changeType = ChangeType.Delete,
                subsets = subsets,
                name = loadedNodeDelete.loadedNode.map(_.name).getOrElse(""),
                before = loadedNodeDelete.loadedNode.map(_.node.raw),
                after = None,
                connectionChanges = Seq.empty,
                roleConnectionChanges = Seq.empty,
                definedInNetworkChanges = Seq.empty,
                tagDiffs = None,
                nodeMoved = None,
                addedToRoute = Seq.empty,
                removedFromRoute = Seq.empty,
                addedToNetwork = Seq.empty,
                removedFromNetwork = Seq.empty,
                factDiffs = FactDiffs(),
                Seq(Fact.WasOrphan, Fact.Deleted)
              )
            )
          )
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

  private def analyzed(nodeChange: NodeChange): NodeChange = {
    new NodeChangeAnalyzer(nodeChange).analyzed()
  }

}
