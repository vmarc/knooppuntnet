package kpn.database.actions.routes

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.mongodb.scala.Document
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.exclude
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryRouteTileNames {
  private val log = Log(classOf[MongoQueryRouteTileNames])
}

class MongoQueryRouteTileNames(database: Database) {

  def execute(routeType: RouteType, log: Log = MongoQueryRouteTileNames.log): Seq[TileId] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("routeTypes", routeType.entryName)
        ),
        project(
          fields(
            exclude("_id"),
            include("z"),
            include("x"),
            include("y"),
          )
        ),
        group(
          Document(
            "z" -> "$z",
            "x" -> "$x",
            "y" -> "$y"
          ),
        ),
        project(
          fields(
            exclude("_id"),
            computed("z", "$_id.z"),
            computed("x", "$_id.x"),
            computed("y", "$_id.y"),
          )
        ),
        sort(
          orderBy(
            ascending(
              "z",
              "x",
              "y",
            )
          )
        )
      )
      val tiles = database.routeTiles.aggregate[TileId](pipeline, log, allowDiskUse = true)
      (s"${tiles.size} tiles", tiles)
    }
  }
}
