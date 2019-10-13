package kpn.server.api.analysis.pages.network

import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.changes.builder.NetworkChangeInfoBuilder
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.network.NetworkChangesPage
import kpn.shared.network.NetworkInfo
import org.springframework.stereotype.Component

@Component
class NetworkChangesPageBuilderImpl(
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends NetworkChangesPageBuilder {

  override def build(user: Option[String], parameters: ChangesParameters): Option[NetworkChangesPage] = {
    val networkId = parameters.networkId.get
    if (networkId == 1) {
      Some(NetworkChangesPageExample.page)
    }
    else {
      buildPage(user, parameters, networkId)
    }
  }

  private def buildPage(user: Option[String], parameters: ChangesParameters, networkId: Long): Option[NetworkChangesPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map { network =>
      buildPageContents(user, parameters, network)
    }
  }

  private def buildPageContents(user: Option[String], parameters: ChangesParameters, networkInfo: NetworkInfo): NetworkChangesPage = {
    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
    val changesFilter = changeSetRepository.networkChangesFilter(networkInfo.attributes.id, parameters.year, parameters.month, parameters.day)
    val totalCount = changesFilter.currentItemCount(parameters.impact)
    val changes: Seq[NetworkChange] = if (user.isDefined) {
      changeSetRepository.networkChanges(parameters)
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
