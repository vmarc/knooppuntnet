package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.common.Time
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNodeChangeCount
import kpn.core.mongo.actions.nodes.MongoQueryNodeChangeCounts
import kpn.core.mongo.actions.nodes.MongoQueryNodeChanges
import org.springframework.stereotype.Component

@Component
class MongoNodeRepositoryImpl(database: Database) extends MongoNodeRepository {

  override def save(node: NodeInfo): Unit = {
    database.nodes.save(node)
  }

  override def nodeWithId(nodeId: Long): Option[NodeInfo] = {
    database.nodes.findById(nodeId)
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
