package kpn.server.repository

import com.mongodb.client.model.Updates.set
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkChangeCounts
import kpn.database.actions.networks.MongoQueryNetworkChanges
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web", "analysis"))
class NetworkInfoRepository(database: Database) {

  private val log = Log(classOf[NetworkInfoRepository])

  def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkChange] = {
    new MongoQueryNetworkChanges(database).execute(networkId, parameters)
  }

  def networkChangesFilter(
    networkId: Long,
    yearOption: Option[Long],
    monthOption: Option[Long],
    dayOption: Option[Long]
  ): Seq[ChangesFilterOption] = {

    val year = yearOption match {
      case Some(longYear) => longYear.toInt
      case None => Time.now.year
    }
    val changeSetCounts = new MongoQueryNetworkChangeCounts(database).execute(networkId, year, monthOption.map(_.toInt))
    changeSetCounts.toFilterOptions(yearOption, monthOption, dayOption)
  }

  def updateNetworkChangeCount(networkId: Long): Unit = {

    val changesCount = {
      val changesFilter = equal("networkId", networkId)
      database.networkChanges.countFilteredDocuments(changesFilter)
    }

    val filter = equal("_id", networkId)
    val update = set("summary.changeCount", changesCount)

    database.networks.updateOne(filter, update, log)
  }
}
