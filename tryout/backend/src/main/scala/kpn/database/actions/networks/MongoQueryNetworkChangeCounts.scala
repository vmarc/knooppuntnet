package kpn.database.actions.networks

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.networks.MongoQueryNetworkChangeCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryNetworkChangeCounts {
  private val log = Log(classOf[MongoQueryNetworkChangeCounts])
}

class MongoQueryNetworkChangeCounts(database: Database) {

  def execute(networkId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {
    ChangeCountPipeline.execute(
      database.networkChanges,
      Seq(filter(equal("networkId", networkId))),
      year,
      monthOption,
      log
    )
  }
}
