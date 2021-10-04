package kpn.server.repository

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.common.Time
import kpn.core.doc.NetworkInfoDoc
import kpn.database.actions.networks.MongoQueryNetworkChangeCounts
import kpn.database.actions.networks.MongoQueryNetworkChanges
import kpn.database.base.Database
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set
import org.springframework.stereotype.Component

@Component
class NetworkInfoRepositoryImpl(database: Database) extends NetworkInfoRepository {

  override def findById(networkId: Long): Option[NetworkInfoDoc] = {
    database.networkInfos.findById(networkId)
  }

  override def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkInfoChange] = {
    new MongoQueryNetworkChanges(database).execute(networkId, parameters)
  }

  override def networkChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val year = yearOption match {
      case None => Time.now.year
      case Some(year) => year.toInt
    }
    val changeSetCounts = new MongoQueryNetworkChangeCounts(database).execute(nodeId, year, monthOption.map(_.toInt))
    ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
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

    database.networkInfos.updateOne(filter, update)
  }
}
