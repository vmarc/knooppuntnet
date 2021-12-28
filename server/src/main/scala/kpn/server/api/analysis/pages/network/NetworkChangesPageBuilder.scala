package kpn.server.api.analysis.pages.network

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkChangesPage
import kpn.core.doc.NetworkInfoDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.changes.builder.NetworkChangeInfoBuilder
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

  def build(user: Option[String], networkId: Long, parameters: ChangesParameters): Option[NetworkChangesPage] = {
    if (networkId == 1) {
      Some(NetworkChangesPageExample.page)
    }
    else {
      buildPage(user, networkId, parameters)
    }
  }

  private def buildPage(
    user: Option[String],
    networkId: Long,
    parameters: ChangesParameters
  ): Option[NetworkChangesPage] = {
    database.networkInfos.findById(networkId, log).map { networkInfoDoc =>
      buildNetworkChangesPage(user, parameters, networkInfoDoc)
    }
  }

  private def buildNetworkChangesPage(
    user: Option[String],
    parameters: ChangesParameters,
    networkInfoDoc: NetworkInfoDoc
  ): NetworkChangesPage = {

    val changes = if (user.isDefined) {
      networkInfoRepository.networkChanges(networkInfoDoc._id, parameters)
    }
    else {
      Seq.empty
    }

    val filterOptions = if (changes.nonEmpty) {
      networkInfoRepository.networkChangesFilter(networkInfoDoc._id, parameters.year, parameters.month, parameters.day)
    }
    else {
      Seq.empty
    }

    val changeCount = {
      if (filterOptions.nonEmpty) {
        val filterOption = filterOptions.find(_.current).getOrElse(filterOptions.head)
        if (parameters.impact) {
          filterOption.impactedCount
        }
        else {
          filterOption.totalCount
        }
      }
      else {
        0L
      }
    }

    val changeSetIds = changes.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds) // TODO include in aggregate !!!
    val networkUpdateInfos = changes.zipWithIndex.map { case (change, index) =>
      val rowIndex = parameters.itemsPerPage * parameters.pageIndex + index
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
