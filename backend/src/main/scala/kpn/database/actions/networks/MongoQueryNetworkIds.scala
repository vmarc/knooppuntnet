package kpn.database.actions.networks

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkIds.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryNetworkIds {
  private val log = Log(classOf[MongoQueryNetworkIds])
}

class MongoQueryNetworkIds(database: Database) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        project(
          fields(
            include("_id")
          )
        )
      )
      val networkIds = database.networks.aggregate(pipeline, classOf[Id], log).map(_._id)
      (s"${networkIds.size} active networks", networkIds)
    }
  }
}
