package kpn.server.api.analysis.pages.network

import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkChangesPage
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkChangesPageData
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
    query(networkId).map { pageData =>
      buildNetworkChangesPage(networkId, parameters, pageData)
    }
  }

  private def buildNetworkChangesPage(
    networkId: Long,
    parameters: ChangesParameters,
    pageData: NetworkChangesPageData
  ): NetworkChangesPage = {

    val filterOptions = if (RequestContext.isLoggedIn) {
      networkInfoRepository.networkChangesFilter(networkId, parameters.year, parameters.month, parameters.day)
    }
    else {
      Seq.empty
    }

    val changes = if (RequestContext.isLoggedIn) {
      networkInfoRepository.networkChanges(networkId, parameters)
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
      pageData.summary,
      filterOptions,
      networkUpdateInfos,
      changeCount
    )
  }

  private def query(networkId: Long): Option[NetworkChangesPageData] = {
    new MongoQueryNetworkChangesPageData(database).execute(networkId)
  }
}
