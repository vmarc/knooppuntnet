package kpn.database.actions.nodes

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.nodes.MongoQueryNodeChangeCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal

object MongoQueryNodeChangeCounts {
  private val log = Log(classOf[MongoQueryNodeChangeCounts])
}

class MongoQueryNodeChangeCounts(database: Database) {

  def execute(nodeId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {
    ChangeCountPipeline.execute(
      database.nodeChanges,
      Some(filter(equal("key.elementId", nodeId))),
      year,
      monthOption,
      log
    )
  }
}
