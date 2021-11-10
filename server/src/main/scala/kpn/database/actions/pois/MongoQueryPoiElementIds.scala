package kpn.database.actions.pois

import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryPoiElementIds.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoQuery
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

object MongoQueryPoiElementIds extends MongoQuery {
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
      val idDocs = database.pois.aggregate[Id](pipeline, log)
      val ids = idDocs.map(_._id).sorted
      (s"elementType '$elementType' ids: ${ids.size}", ids)
    }
  }
}
