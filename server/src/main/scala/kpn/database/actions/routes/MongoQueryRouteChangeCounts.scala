package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.routes.MongoQueryRouteChangeCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal

object MongoQueryRouteChangeCounts {
  private val log = Log(classOf[MongoQueryRouteChangeCounts])
}

class MongoQueryRouteChangeCounts(database: Database) {

  def execute(routeId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {
    ChangeCountPipeline.execute(
      database.routeChanges,
      Some(filter(equal("key.elementId", routeId))),
      year,
      monthOption,
      log
    )
  }
}
