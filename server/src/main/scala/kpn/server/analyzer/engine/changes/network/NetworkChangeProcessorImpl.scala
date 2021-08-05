package kpn.server.analyzer.engine.changes.network

import kpn.core.mongo.doc.NetworkDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.overpass.OverpassRepository
import org.springframework.stereotype.Component

@Component
class NetworkChangeProcessorImpl(
  changeAnalyzer: NetworkChangeAnalyzer,
  overpassRepository: OverpassRepository
) extends NetworkChangeProcessor {

  private val log = Log(classOf[NetworkChangeProcessorImpl])

  def process(context: ChangeSetContext): ChangeSetChanges = {
    log.debugElapsed {
      val batchSize = 100
      val networkChanges = changeAnalyzer.analyze(context.changeSet)
      val allNetworkIds = networkChanges.elementIds
      val networkChangeDatas = allNetworkIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (networkIds, index) =>
        Log.context(s"$index/${allNetworkIds.size}") {
          process(context, networkChanges, networkIds)
        }
      }.toSeq
      ("", ChangeSetChanges(networkChangeDatas = networkChangeDatas))
    }
  }

  private def process(context: ChangeSetContext, networkChanges: ElementChanges, networkIds: Seq[Long]): Seq[NetworkChangeData] = {
    val beforeRelations = overpassRepository.relations(context.timestampBefore, networkIds)
    val afterRelations = overpassRepository.relations(context.timestampAfter, networkIds)
    networkIds.map { networkId =>
      val changeAction = networkChanges.action(networkId)
      NetworkChangeData(
        networkId,
        changeAction,
        beforeRelations.find(_.id == networkId).map(NetworkDoc.from),
        afterRelations.find(_.id == networkId).map(NetworkDoc.from)
      )
    }
  }

}
