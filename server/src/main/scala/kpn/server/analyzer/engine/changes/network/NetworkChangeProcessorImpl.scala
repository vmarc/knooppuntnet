package kpn.server.analyzer.engine.changes.network

import kpn.api.common.NetworkFacts
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.diff.IdDiffs
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkNameAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.overpass.OverpassRepository
import org.springframework.stereotype.Component

@Component
class NetworkChangeProcessorImpl(
  database: Database,
  analysisContext: AnalysisContext,
  changeAnalyzer: NetworkChangeAnalyzer,
  overpassRepository: OverpassRepository
) extends NetworkChangeProcessor {

  private val log = Log(classOf[NetworkChangeProcessorImpl])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {
      val batchSize = 100
      val elementChanges = changeAnalyzer.analyze(context)
      val changedNetworkIds = elementChanges.elementIds
      if (changedNetworkIds.nonEmpty) {
        log.info(s"${changedNetworkIds.size} network(s) impacted: ${changedNetworkIds.mkString(", ")}")
      }

      val networkChanges = changedNetworkIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (networkIds, index) =>
        Log.context(s"$index/${changedNetworkIds.size}") {
          process(context, elementChanges, networkIds)
        }
      }.toSeq

      (
        s"${networkChanges.size} network changes",
        context.copy(
          changes = context.changes.copy(
            networkChanges = networkChanges
          )
        )
      )
    }
  }

  private def process(context: ChangeSetContext, networkChanges: ElementChanges, networkIds: Seq[Long]): Seq[NetworkChange] = {
    val beforeRelations = overpassRepository.relations(context.timestampBefore, networkIds)
    val afterRelations = overpassRepository.relations(context.timestampAfter, networkIds)
    networkIds.flatMap { networkId =>
      val changeAction = networkChanges.action(networkId)

      val relationBeforeOption = beforeRelations.find(_.id == networkId)
      val relationAfterOption = afterRelations.find(_.id == networkId)
      // afterRelations.find(_.id == networkId).map(NetworkDoc.from)

      relationBeforeOption match {
        case None =>
          relationAfterOption match {
            case None =>
              // TODO message ?
              None
            case Some(after) =>
              Some(processCreate(context, after))
          }
        case Some(before) =>
          relationAfterOption match {
            case None =>
              Some(processDelete(context, before))
            case Some(after) =>
              Some(processUpdate(context, before, after))
          }
      }
    }
  }

  def processCreate(context: ChangeSetContext, after: RawRelation): NetworkChange = {

    analysisContext.watched.networks.add(after.id)
    database.networks.save(NetworkDoc.from(after))

    val key = context.buildChangeKey(after.id)

    NetworkChange(
      _id = key.toId,
      key = key,
      networkId = after.id,
      networkName = NetworkNameAnalyzer.name(after.tags),
      changeType = ChangeType.Create,
      nodes = IdDiffs(added = after.nodeMembers.map(_.ref)),
      ways = IdDiffs(added = after.wayMembers.map(_.ref)),
      relations = IdDiffs(added = after.relationMembers.map(_.ref))
    )
  }

  def processDelete(context: ChangeSetContext, before: RawRelation): NetworkChange = {
    analysisContext.watched.networks.delete(before.id)
    database.networks.save(
      NetworkDoc(
        before.id,
        active = false,
        context.changeSet.timestamp,
        Seq.empty,
        Seq.empty,
        Seq.empty,
        NetworkFacts(),
        before.tags
      )
    )
    val key = context.buildChangeKey(before.id)

    NetworkChange(
      _id = key.toId,
      key = key,
      networkId = before.id,
      networkName = NetworkNameAnalyzer.name(before.tags),
      changeType = ChangeType.Create,
      nodes = IdDiffs(removed = before.nodeMembers.map(_.ref)),
      ways = IdDiffs(removed = before.wayMembers.map(_.ref)),
      relations = IdDiffs(removed = before.relationMembers.map(_.ref))
    )
  }

  def processUpdate(context: ChangeSetContext, before: RawRelation, after: RawRelation): NetworkChange = {
    analysisContext.watched.networks.add(after.id)
    database.networks.save(NetworkDoc.from(after))
    val key = context.buildChangeKey(after.id)

    val nodesBefore = before.nodeMembers.map(_.ref).toSet
    val nodesAfter = after.nodeMembers.map(_.ref).toSet
    val nodesAdded = (nodesAfter -- nodesBefore).toSeq.sorted
    val nodesRemoved = (nodesBefore -- nodesAfter).toSeq.sorted

    val waysBefore = before.wayMembers.map(_.ref).toSet
    val waysAfter = after.wayMembers.map(_.ref).toSet
    val waysAdded = (waysAfter -- waysBefore).toSeq.sorted
    val waysRemoved = (waysBefore -- waysAfter).toSeq.sorted

    val relationsBefore = before.relationMembers.map(_.ref).toSet
    val relationsAfter = after.relationMembers.map(_.ref).toSet
    val relationsAdded = (relationsAfter -- relationsBefore).toSeq.sorted
    val relationsRemoved = (relationsBefore -- relationsAfter).toSeq.sorted

    NetworkChange(
      _id = key.toId,
      key = key,
      networkId = after.id,
      networkName = NetworkNameAnalyzer.name(after.tags),
      changeType = ChangeType.Create,
      nodes = IdDiffs(
        added = nodesAdded,
        removed = nodesRemoved
      ),
      ways = IdDiffs(
        added = waysAdded,
        removed = waysRemoved
      ),
      relations = IdDiffs(
        added = relationsAdded,
        removed = relationsRemoved
      )
    )
  }
}
