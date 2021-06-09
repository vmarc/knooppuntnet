package kpn.server.api.analysis.pages.network

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkChangesPage
import kpn.api.common.network.NetworkInfo
import kpn.server.analyzer.engine.changes.builder.NetworkChangeInfoBuilder
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.MongoNetworkRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkChangesPageBuilderImpl(
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository,
  // new
  mongoEnabled: Boolean,
  mongoNetworkRepository: MongoNetworkRepository

) extends NetworkChangesPageBuilder {

  override def build(user: Option[String], networkId: Long, parameters: ChangesParameters): Option[NetworkChangesPage] = {
    if (networkId == 1) {
      Some(NetworkChangesPageExample.page)
    }
    else {
      if (mongoEnabled) {
        mongoBuildPage(user, networkId, parameters)
      }
      else {
        oldBuildPage(user, networkId, parameters)
      }
    }
  }

  private def mongoBuildPage(user: Option[String], networkId: Long, parameters: ChangesParameters): Option[NetworkChangesPage] = {
    mongoNetworkRepository.networkWithId(networkId).map { network =>
      mongoBuildPageContents(user, parameters, network)
    }
  }

  private def mongoBuildPageContents(user: Option[String], parameters: ChangesParameters, networkInfo: NetworkInfo): NetworkChangesPage = {
    val changeCount = mongoNetworkRepository.networkChangeCount(networkInfo.attributes.id)
    val changesFilter = mongoNetworkRepository.networkChangesFilter(networkInfo.attributes.id, parameters.year, parameters.month, parameters.day)
    val totalCount = changesFilter.currentItemCount(parameters.impact)
    val changes = if (user.isDefined) {
      mongoNetworkRepository.networkChanges(networkInfo.attributes.id, parameters)
    }
    else {
      Seq()
    }

    val changeSetIds = changes.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds) // TODO include in aggregate !!!
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
      Seq()
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
