package kpn.database.actions.pois

import kpn.database.actions.pois.MongoQueryTilePois.log
import kpn.database.actions.pois.MongoQueryTilePois.pipelineString
import kpn.database.base.Database
import kpn.database.base.MongoQuery
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
