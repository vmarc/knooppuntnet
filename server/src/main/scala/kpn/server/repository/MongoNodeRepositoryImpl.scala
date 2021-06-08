package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.common.Time
import kpn.core.database.doc.NodeDoc
import kpn.core.mongo.changes.MongoQueryNodeChangeCount
import kpn.core.mongo.changes.MongoQueryNodeChangeCounts
import kpn.core.mongo.changes.MongoQueryNodeChanges
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Component
class MongoNodeRepositoryImpl(database: MongoDatabase) extends MongoNodeRepository {

  override def nodeWithId(nodeId: Long): Option[NodeInfo] = {
    val collection = database.getCollection[NodeDoc]("nodes")
    val future = collection.find[NodeDoc](equal("_id", s"node:$nodeId")).headOption()
    Await.result(future, Duration(5, TimeUnit.SECONDS)).map(_.node)
  }

  override def nodeChangeCount(nodeId: Long): Long = {
    new MongoQueryNodeChangeCount(database).execute(nodeId)
  }

  override def nodeChanges(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange] = {
    new MongoQueryNodeChanges(database).execute(nodeId, parameters)
  }

  override def nodeChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val year = yearOption match {
      case None => Time.now.year
      case Some(year) => year.toInt
    }
    val changeSetCounts = new MongoQueryNodeChangeCounts(database).execute(nodeId, year, monthOption.map(_.toInt))
    ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
  }
}
