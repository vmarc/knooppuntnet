package kpn.server.analyzer.engine.changes.network

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNodeNetworkReferences
import kpn.core.mongo.actions.routes.MongoQueryRouteNetworkReferences
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.server.analyzer.engine.analysis.network.info.NetworkInfoMasterAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.stereotype.Component

@Component
class NetworkInfoChangeProcessorImpl(
  database: Database,
  networkInfoMasterAnalyzer: NetworkInfoMasterAnalyzer
) extends NetworkInfoChangeProcessor {

  def analyze(context: ChangeSetContext): ChangeSetContext = {
    val impactedNetworkIds1 = context.changes.newNetworkChanges.map(_.networkId)
    val impactedRouteIds = context.changes.routeChanges.map(_.id)
    val impactedNetworkIds2 = new MongoQueryRouteNetworkReferences(database).executeRouteIds(impactedRouteIds)
    val impactedNodeIds1 = context.changes.newNetworkChanges.flatMap(_.impactedNodeIds)
    val impactedNodeIds2 = context.changes.routeChanges.flatMap(_.impactedNodeIds)
    val impactedNodeIds3 = context.changes.nodeChanges.map(_.id)
    val impactedNodeIds = (impactedNodeIds1 ++ impactedNodeIds2 ++ impactedNodeIds3).distinct.sorted
    val impactedNetworkIds3 = new MongoQueryNodeNetworkReferences(database).executeNodeIds(impactedNodeIds)
    val impactedNetworkIds = (impactedNetworkIds1 ++ impactedNetworkIds2 ++ impactedNetworkIds3).distinct.sorted

    val networkChanges = impactedNetworkIds.flatMap { networkId =>
      val beforeOption = database.networkInfos.findById(networkId)
      val afterOption = networkInfoMasterAnalyzer.updateNetwork(networkId)
      beforeOption match {
        case None =>
          afterOption match {
            case None => None // TODO message ?
            case Some(after) =>
              processCreate(context, after)
          }
        case Some(before) =>
          afterOption match {
            case None => processDelete(context, before)
            case Some(after) => processUpdate(context, before, after)
          }
      }
    }

    context.copy(
      changes = context.changes.copy(
        networkChanges = networkChanges
      )
    )
  }

  private def processCreate(context: ChangeSetContext, after: NetworkInfoDoc): Option[NetworkChange] = {
    val key = context.buildChangeKey(after._id)
    Some(
      NetworkChange(
        key.toId,
        key,
        changeType = ChangeType.Create,
        country = after.country,
        networkType = after.scopedNetworkType.networkType,
        networkId = after._id,
        networkName = after.summary.name,
        orphanRoutes = RefChanges(), // ??
        orphanNodes = RefChanges(), // ??
        networkDataUpdate = None, // Option[NetworkDataUpdate],
        networkNodes = RefDiffs(), // ??
        routes = RefDiffs(), // ??
        nodes = IdDiffs(), // ??
        ways = IdDiffs(), // ??
        relations = IdDiffs(), // ??
        happy = true, // ??
        investigate = false, // ??
        impact = true // ??
      )
    )
  }

  private def processDelete(context: ChangeSetContext, before: NetworkInfoDoc): Option[NetworkChange] = {
    None
  }

  private def processUpdate(context: ChangeSetContext, before: NetworkInfoDoc, after: NetworkInfoDoc): Option[NetworkChange] = {
    None
  }
}
