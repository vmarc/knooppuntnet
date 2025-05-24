package kpn.database.actions.routes

import kpn.api.common.changes.details.BaseRouteChange
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.in

class MongoQueryBaseRouteChanges(database: Database) {

  private val log = Log(classOf[MongoQueryBaseRouteChanges])

  def execute(ids: Seq[String]): Seq[BaseRouteChange] = {
    val pipeline = buildPipeline(ids)
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    log.debugElapsed {
      val routeChanges = database.baseRouteChanges.aggregate[BaseRouteChange](pipeline)
      (s"${routeChanges.size} base route changes", routeChanges)
    }
  }

  private def buildPipeline(ids: Seq[String]): MongoPipeline = {
    Seq(
      filter(
        in("_id", ids: _*)
      )
    )
  }
}
