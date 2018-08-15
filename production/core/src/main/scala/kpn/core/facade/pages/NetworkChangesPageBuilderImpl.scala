package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.engine.changes.builder.NetworkChangeInfoBuilder
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.NetworkRepository
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.network.NetworkChangesPage

class NetworkChangesPageBuilderImpl(
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends NetworkChangesPageBuilder {

  override def build(user: Option[String], parameters: ChangesParameters): Option[NetworkChangesPage] = {
    val networkId = parameters.networkId.get
    networkRepository.network(networkId, Couch.uiTimeout).map { network =>
      val changesFilter = changeSetRepository.networkChangesFilter(networkId, parameters.year, parameters.month, parameters.day)
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
}
