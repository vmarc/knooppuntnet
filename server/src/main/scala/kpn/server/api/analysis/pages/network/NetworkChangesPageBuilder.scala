package kpn.server.api.analysis.pages.network

import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkChangesPage
import kpn.core.doc.NetworkInfoDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.changes.builder.NetworkChangeInfoBuilder
import kpn.server.config.RequestContext
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.NetworkInfoRepository
import org.springframework.stereotype.Component

@Component
class NetworkChangesPageBuilder(
  database: Database,
  changeSetInfoRepository: ChangeSetInfoRepository,
  networkInfoRepository: NetworkInfoRepository
) {

  private val log = Log(classOf[NetworkDetailsPageBuilder])

  def build(networkId: Long, parameters: ChangesParameters): Option[NetworkChangesPage] = {
    if (networkId == 1) {
      Some(NetworkChangesPageExample.page)
    }
    else {
      buildPage(networkId, parameters)
    }
  }

  private def buildPage(
    networkId: Long,
    parameters: ChangesParameters
  ): Option[NetworkChangesPage] = {
    database.networkInfos.findById(networkId, log).map { networkInfoDoc =>
      buildNetworkChangesPage(parameters, networkInfoDoc)
    }
  }

  private def buildNetworkChangesPage(
    parameters: ChangesParameters,
    networkInfoDoc: NetworkInfoDoc
  ): NetworkChangesPage = {

    val filterOptions = if (RequestContext.isLoggedIn) {
      networkInfoRepository.networkChangesFilter(networkInfoDoc._id, parameters.year, parameters.month, parameters.day)
    }
    else {
      Seq.empty
    }

    val changes = if (RequestContext.isLoggedIn) {
      networkInfoRepository.networkChanges(networkInfoDoc._id, parameters)
    }
    else {
      Seq.empty
    }

    val changeCount = ChangesFilterOption.changesCount(filterOptions, parameters)
    val changeSetIds = changes.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds) // TODO include in aggregate !!!
    val networkUpdateInfos = changes.zipWithIndex.map { case (change, index) =>
      val rowIndex = parameters.pageSize * parameters.pageIndex + index
      new NetworkChangeInfoBuilder().build(rowIndex, change, changeSetInfos)
    }
    NetworkChangesPage(
      networkInfoDoc.summary,
      filterOptions,
      networkUpdateInfos,
      changeCount
    )
  }
}
