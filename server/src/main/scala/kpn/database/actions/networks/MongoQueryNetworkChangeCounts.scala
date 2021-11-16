package kpn.database.actions.networks

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.networks.MongoQueryNetworkChangeCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal

object MongoQueryNetworkChangeCounts {
  private val log = Log(classOf[MongoQueryNetworkChangeCounts])
}

class MongoQueryNetworkChangeCounts(database: Database) {

  def execute(networkId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {
    ChangeCountPipeline.execute(
      database.networkInfoChanges,
      Some(filter(equal("networkId", networkId))),
      year,
      monthOption,
      log
    )
  }
}
