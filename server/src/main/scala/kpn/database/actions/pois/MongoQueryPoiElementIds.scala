package kpn.database.actions.pois

import kpn.database.actions.pois.MongoQueryPoiElementIds.log
import kpn.database.actions.pois.MongoQueryPoiElementIds.pipelineString
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoQuery
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
