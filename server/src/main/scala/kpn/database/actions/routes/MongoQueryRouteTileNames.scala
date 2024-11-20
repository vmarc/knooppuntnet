package kpn.database.actions.routes

import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.StringId
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.exclude
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteTileNames {
  private val log = Log(classOf[MongoQueryRouteTileNames])
}

class MongoQueryRouteTileNames(database: Database) {

  def execute(networkType: NetworkType, log: Log = MongoQueryRouteTileNames.log): Seq[String] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("networkTypes", networkType.name)
        ),
        project(
          fields(
            exclude("_id"),
            include("tile"),
          )
        ),
        group(
          "$tile"
        ),
      )
      val ids = database.routeTiles.aggregate[StringId](pipeline, log).map(_._id)
      (s"${ids.size} tiles", ids)
    }
  }
}
