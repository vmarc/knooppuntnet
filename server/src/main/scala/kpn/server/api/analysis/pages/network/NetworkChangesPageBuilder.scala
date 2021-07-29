package kpn.server.api.analysis.pages.network

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkChangesPage
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkSummary
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.builder.NetworkChangeInfoBuilder
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.MongoNetworkRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkChangesPageBuilder(
  mongoEnabled: Boolean,
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository,
  // new
  database: Database,
  mongoNetworkRepository: MongoNetworkRepository
) {

  private val log = Log(classOf[NetworkDetailsPageBuilder])

  def build(user: Option[String], networkId: Long, parameters: ChangesParameters): Option[NetworkChangesPage] = {
    if (networkId == 1) {
      Some(NetworkChangesPageExample.page)
    }
    else {
      if (mongoEnabled) {
        buildPage(user, networkId, parameters)
      }
      else {
        oldBuildPage(user, networkId, parameters)
      }
    }
  }

  private def buildPage(user: Option[String], networkId: Long, parameters: ChangesParameters): Option[NetworkChangesPage] = {
    database.networkInfos.findById(networkId, log).map { networkInfoDoc =>
      buildNetworkChangesPage(user, parameters, networkInfoDoc)
    }
  }

  private def buildNetworkChangesPage(user: Option[String], parameters: ChangesParameters, networkInfoDoc: NetworkInfoDoc): NetworkChangesPage = {
    val changesFilter = mongoNetworkRepository.networkChangesFilter(networkInfoDoc._id, parameters.year, parameters.month, parameters.day)
    val totalCount = changesFilter.currentItemCount(parameters.impact)
    val changes = if (user.isDefined) {
      mongoNetworkRepository.networkChanges(networkInfoDoc._id, parameters)
    }
    else {
      Seq.empty
    }

    val changeSetIds = changes.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds) // TODO include in aggregate !!!
    val networkUpdateInfos = changes.map { change =>
      new NetworkChangeInfoBuilder().build(change, changeSetInfos)
    }
    NetworkChangesPage(
      networkInfoDoc.summary,
      changesFilter,
      networkUpdateInfos,
      totalCount
    )
  }

  private def oldBuildPage(user: Option[String], networkId: Long, parameters: ChangesParameters): Option[NetworkChangesPage] = {
    networkRepository.network(networkId).map { network =>
      oldBuildPageContents(user, parameters, network)
    }
  }

  private def oldBuildPageContents(user: Option[String], parameters: ChangesParameters, networkInfo: NetworkInfo): NetworkChangesPage = {
    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
    val changesFilter = changeSetRepository.networkChangesFilter(networkInfo.attributes.id, parameters.year, parameters.month, parameters.day)
    val totalCount = changesFilter.currentItemCount(parameters.impact)
    val changes = if (user.isDefined) {
      changeSetRepository.networkChanges(networkInfo.attributes.id, parameters)
    }
    else {
      Seq.empty
    }

    val changeSetIds = changes.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds)
    val networkUpdateInfos = changes.map { change =>
      new NetworkChangeInfoBuilder().build(change, changeSetInfos)
    }
    NetworkChangesPage(
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      changesFilter,
      networkUpdateInfos,
      totalCount
    )
  }
}
