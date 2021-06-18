package kpn.core.mongo.actions.pois

import kpn.core.mongo.Database
import kpn.core.mongo.actions.pois.MongoQueryTilePois.log
import kpn.core.mongo.actions.pois.MongoQueryTilePois.pipelineString
import kpn.core.mongo.util.MongoQuery
import kpn.core.poi.PoiInfo
import kpn.core.util.Log

object MongoQueryTilePois extends MongoQuery {
  private val log = Log(classOf[MongoQueryTilePois])
  private val pipelineString = readPipelineString("pipeline")
}

class MongoQueryTilePois(database: Database) {
  def execute(tileName: String): Seq[PoiInfo] = {
    log.debugElapsed {
      val args = Map("@tileName" -> tileName)
      val poiInfos = database.pois.stringPipelineAggregate[PoiInfo](pipelineString, args, log)
      (s"poiInfos: ${poiInfos.size}", poiInfos)
    }
  }
}
