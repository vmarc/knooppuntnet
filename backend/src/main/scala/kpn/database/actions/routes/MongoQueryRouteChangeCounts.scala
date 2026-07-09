package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.routes.MongoQueryRouteChangeCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryRouteChangeCounts {
  private val log = Log(classOf[MongoQueryRouteChangeCounts])
}

class MongoQueryRouteChangeCounts(database: Database) {

  def execute(routeId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {
    ChangeCountPipeline.execute(
      database.routeChanges,
      Seq(filter(equal("key.elementId", routeId))),
      year,
      monthOption,
      log
    )
  }
}
