package kpn.core.facade.pages.network

import kpn.core.db.couch.Couch
import kpn.core.engine.changes.builder.NetworkChangeInfoBuilder
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.NetworkRepository
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.network.NetworkChangesPage
import kpn.shared.network.NetworkInfo

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

  private def buildPageContents(user: Option[String], parameters: ChangesParameters, network: NetworkInfo): NetworkChangesPage = {
    val changesFilter = changeSetRepository.networkChangesFilter(network.attributes.id, parameters.year, parameters.month, parameters.day)
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
    NetworkChangesPage(network, changesFilter, networkUpdateInfos, totalCount)
  }

}
