package kpn.database.actions.pois

import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryTilePois.log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryTilePois {
  private val log = Log(classOf[MongoQueryTilePois])
}

class MongoQueryTilePois(database: Database) {
  def execute(tileName: String): Seq[PoiInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(in("tiles", tileName)),
        unwind("$layers"),
        project(
          fields(
            excludeId(),
            include("elementType"),
            include("elementId"),
            include("latitude"),
            include("longitude"),
            computed("layer", "$layers")
          )
        )
      )
      val poiInfos = database.pois.aggregate[PoiInfo](pipeline, log)
      (s"poiInfos: ${poiInfos.size}", poiInfos)
    }
  }
}
