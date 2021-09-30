package kpn.database.actions.networks

import kpn.database.actions.networks.MongoQueryNetworkIds.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

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
      val networkIds = database.networks.aggregate[Id](pipeline, log).map(_._id)
      (s"${networkIds.size} active networks", networkIds)
    }
  }
}
