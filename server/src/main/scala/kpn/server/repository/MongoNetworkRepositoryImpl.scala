package kpn.server.repository

import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkInfo
import kpn.core.common.Time
import kpn.core.database.doc.NetworkDoc
import kpn.core.mongo.changes.MongoQueryNetworkChangeCount
import kpn.core.mongo.changes.MongoQueryNetworkChangeCounts
import kpn.core.mongo.changes.MongoQueryNetworkChanges
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Component
class MongoNetworkRepositoryImpl(database: MongoDatabase) extends MongoNetworkRepository {

  override def networkWithId(networkId: Long): Option[NetworkInfo] = {
    val collection = database.getCollection[NetworkDoc]("networks")
    val future = collection.find[NetworkDoc](equal("_id", s"network:$networkId")).headOption()
    Await.result(future, Duration(5, TimeUnit.SECONDS)).map(_.network)
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
