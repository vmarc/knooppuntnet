package kpn.database.actions.pois

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryPoiElementIds.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryPoiElementIds {
  private val log = Log(classOf[MongoQueryPoiElementIds])
}

class MongoQueryPoiElementIds(database: Database) {
  def execute(elementType: String): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("elementType", elementType)),
        project(
          fields(
            excludeId(),
            computed("_id", "$elementId")
          )
        )
      )
      val idDocs = database.pois.aggregate(pipeline, classOf[Id], log)
      val ids = idDocs.map(_._id).sorted
      (s"elementType '$elementType' ids: ${ids.size}", ids)
    }
  }
}
