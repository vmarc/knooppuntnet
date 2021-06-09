package kpn.server.repository

import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkInfo
import kpn.core.common.Time
import kpn.core.mongo.changes.MongoQueryNetwork
import kpn.core.mongo.changes.MongoQueryNetworkChangeCount
import kpn.core.mongo.changes.MongoQueryNetworkChangeCounts
import kpn.core.mongo.changes.MongoQueryNetworkChanges
import org.mongodb.scala.MongoDatabase
import org.springframework.stereotype.Component

@Component
class MongoNetworkRepositoryImpl(database: MongoDatabase) extends MongoNetworkRepository {

  override def networkWithId(networkId: Long): Option[NetworkInfo] = {
    new MongoQueryNetwork(database).execute(networkId)
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
