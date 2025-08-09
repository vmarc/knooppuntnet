package kpn.database.actions.pois

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.poi.PoiInfo
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryTilePois.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.filter

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
      val poiInfos = database.pois.aggregate(pipeline, classOf[PoiInfo], log)
      (s"poiInfos: ${poiInfos.size}", poiInfos)
    }
  }
}
