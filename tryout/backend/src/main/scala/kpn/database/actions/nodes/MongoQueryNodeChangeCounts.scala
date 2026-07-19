package kpn.database.actions.nodes

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.nodes.MongoQueryNodeChangeCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryNodeChangeCounts {
  private val log = Log(classOf[MongoQueryNodeChangeCounts])
}

class MongoQueryNodeChangeCounts(database: Database) {

  def execute(nodeId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {
    ChangeCountPipeline.execute(
      database.nodeChanges,
      Seq(filter(equal("key.elementId", nodeId))),
      year,
      monthOption,
      log
    )
  }
}
