package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.common.Time
import kpn.core.mongo.changes.MongoQueryNode
import kpn.core.mongo.changes.MongoQueryNodeChangeCount
import kpn.core.mongo.changes.MongoQueryNodeChangeCounts
import kpn.core.mongo.changes.MongoQueryNodeChanges
import kpn.core.mongo.changes.MongoSaveNode
import org.mongodb.scala.MongoDatabase
import org.springframework.stereotype.Component

@Component
class MongoNodeRepositoryImpl(database: MongoDatabase) extends MongoNodeRepository {

  override def save(node: NodeInfo): Unit = {
    new MongoSaveNode(database).execute(node)
  }

  override def nodeWithId(nodeId: Long): Option[NodeInfo] = {
    new MongoQueryNode(database).execute(nodeId)
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
