package kpn.server.analyzer.engine.changes.network

import kpn.api.common.data.raw.RawRelation
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.custom.ChangeType
import kpn.api.custom.Tags
import kpn.core.analysis.TagInterpreter
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkNameAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.RawRelationChange
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

      relationBeforeOption match {
        case None =>
          relationAfterOption match {
            case Some(after) => Some(processCreate(context, after))
            case None => None
          }
        case Some(before) =>
          relationAfterOption match {
            case None => Some(processDelete(context, before))
            case Some(after) =>
              if (TagInterpreter.isNetworkRelation(after.tags)) {
                Some(processUpdate(context, before, after))
              }
              else {
                Some(processDelete(context, before))
              }
          }
      }
    }
  }

  def processCreate(context: ChangeSetContext, after: RawRelation): NetworkChange = {

    analysisContext.watched.networks.add(after.id)
    database.networks.save(NetworkDoc.from(after))

    val key = context.buildChangeKey(after.id)
    val networkNameAfter = NetworkNameAnalyzer.name(after.tags)
    val networkDataUpdate = NetworkDataUpdate(
      None,
      Some(
        NetworkData(
          after.toMeta,
          networkNameAfter
        )
      )
    )

    NetworkChange(
      _id = key.toId,
      key = key,
      networkId = after.id,
      networkName = networkNameAfter,
      changeType = ChangeType.Create,
      networkDataUpdate = Some(networkDataUpdate),
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
        before.version,
        before.changeSetId,
        context.changeSet.timestamp,
        Seq.empty,
        Seq.empty,
        Seq.empty,
        before.tags
      )
    )

    val key = context.buildChangeKey(before.id)
    val networkNameBefore = NetworkNameAnalyzer.name(before.tags)
    val networkDataUpdate = NetworkDataUpdate(
      Some(
        NetworkData(
          before.toMeta,
          networkNameBefore
        )
      ),
      None
    )

    NetworkChange(
      _id = key.toId,
      key = key,
      networkId = before.id,
      networkName = networkNameBefore,
      changeType = ChangeType.Create,
      networkDataUpdate = Some(networkDataUpdate),
      nodes = IdDiffs(removed = before.nodeMembers.map(_.ref)),
      ways = IdDiffs(removed = before.wayMembers.map(_.ref)),
      relations = IdDiffs(removed = before.relationMembers.map(_.ref))
    )
  }

  def processUpdate(context: ChangeSetContext, before: RawRelation, after: RawRelation): NetworkChange = {

    context.elementChanges.relationAdd(RawRelationChange(before, after))

    analysisContext.watched.networks.add(after.id)
    database.networks.save(NetworkDoc.from(after))

    val key = context.buildChangeKey(after.id)

    val relationDiffAnalyzer = new NetworkRelationDiffAnalyzer(before, after)

    val networkNameBefore = NetworkNameAnalyzer.name(before.tags)
    val networkNameAfter = NetworkNameAnalyzer.name(after.tags)
    val metaBefore = before.toMeta
    val metaAfter = before.toMeta

    val networkDataUpdate = if (networkNameBefore != networkNameAfter || metaBefore != metaAfter) {
      Some(
        NetworkDataUpdate(
          Some(
            NetworkData(
              before.toMeta,
              networkNameBefore
            )
          ),
          Some(
            NetworkData(
              after.toMeta,
              networkNameAfter
            )
          )
        )
      )
    }
    else {
      None
    }

    NetworkChange(
      _id = key.toId,
      key = key,
      networkId = after.id,
      networkName = networkNameAfter,
      changeType = ChangeType.Create,
      networkDataUpdate = networkDataUpdate,
      nodes = relationDiffAnalyzer.nodeDiffs,
      ways = relationDiffAnalyzer.wayDiffs,
      relations = relationDiffAnalyzer.relationDiffs
    )
  }
}
