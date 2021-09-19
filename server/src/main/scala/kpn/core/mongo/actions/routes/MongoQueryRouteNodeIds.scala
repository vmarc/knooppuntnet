package kpn.core.mongo.actions.routes

import kpn.core.mongo.Database
import kpn.core.mongo.actions.routes.MongoQueryRouteNodeIds.log
import kpn.core.mongo.doc.Label
import kpn.core.mongo.util.Id
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryRouteNodeIds {
  private val log = Log(classOf[MongoQueryRouteNodeIds])
}

// TODO MONGO cleanup - no longer used?
class MongoQueryRouteNodeIds(database: Database) {

  // collect ids of nodes referenced in multiple routes
  def execute(routeIds: Seq[Long]): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            in("_id", routeIds: _*),
          )
        ),
        unwind("$nodeRefs"),
        group("$nodeRefs"),
        sort(orderBy(ascending("_id")))
      )
      val ids = database.routes.aggregate[Id](pipeline, log)
      (s"route node ids: ${ids.size}", ids.map(_._id))
    }
  }
}
