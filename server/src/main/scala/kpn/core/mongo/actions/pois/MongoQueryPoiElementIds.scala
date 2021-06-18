package kpn.core.mongo.actions.pois

import kpn.core.mongo.Database
import kpn.core.mongo.actions.pois.MongoQueryPoiElementIds.log
import kpn.core.mongo.actions.pois.MongoQueryPoiElementIds.pipelineString
import kpn.core.mongo.util.Id
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log

object MongoQueryPoiElementIds extends MongoQuery {
  private val log = Log(classOf[MongoQueryPoiElementIds])
  private val pipelineString = readPipelineString("pipeline")
}

class MongoQueryPoiElementIds(database: Database) {
  def execute(elementType: String): Seq[Long] = {
    log.debugElapsed {
      val args = Map("@elementType" -> elementType)
      val idDocs = database.pois.stringPipelineAggregate[Id](pipelineString, args, log)
      val ids = idDocs.map(_._id).sorted
      (s"elementType '$elementType' ids: ${ids.size}", ids)
    }
  }
}
