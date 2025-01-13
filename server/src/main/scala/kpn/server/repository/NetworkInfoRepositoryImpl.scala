package kpn.server.repository

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkChangeCounts
import kpn.database.actions.networks.MongoQueryNetworkChanges
import kpn.database.base.Database
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set
import org.springframework.stereotype.Component

@Component
class NetworkInfoRepositoryImpl(database: Database) extends NetworkInfoRepository {

  private val log = Log(classOf[NetworkInfoRepositoryImpl])

  override def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkInfoChange] = {
    new MongoQueryNetworkChanges(database).execute(networkId, parameters)
  }

  override def networkChangesFilter(
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
      database.networkInfoChanges.countDocuments(changesFilter)
    }

    val filter = equal("_id", networkId)
    val update = Seq(
      set("summary.changeCount", changesCount)
    )

    database.networks.updateOne(filter, update, log)
  }
}
