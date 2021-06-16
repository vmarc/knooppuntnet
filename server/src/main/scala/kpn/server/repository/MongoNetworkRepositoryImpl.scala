package kpn.server.repository

import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkInfo
import kpn.core.common.Time
import kpn.core.mongo.Database
import kpn.core.mongo.actions.networks.MongoQueryNetworkChangeCount
import kpn.core.mongo.actions.networks.MongoQueryNetworkChangeCounts
import kpn.core.mongo.actions.networks.MongoQueryNetworkChanges
import org.springframework.stereotype.Component

@Component
class MongoNetworkRepositoryImpl(database: Database) extends MongoNetworkRepository {

  override def save(network: NetworkInfo): Unit = {
    database.networks.save(network)
  }

  override def networkWithId(networkId: Long): Option[NetworkInfo] = {
    database.networks.findById(networkId)
  }

  override def networkChangeCount(networkId: Long): Long = {
    new MongoQueryNetworkChangeCount(database).execute(networkId)
  }

  override def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkChange] = {
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
}
