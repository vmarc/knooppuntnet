package kpn.database.actions.pois

import kpn.api.common.poi.LocationPoiLayerCount
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryLocationPoiLayerCounts.log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryLocationPoiLayerCounts {
  private val log = Log(classOf[MongoQueryLocationPois])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      val result = new MongoQueryLocationPoiLayerCounts(database).execute("be-2-11016")
      result.foreach(println)
    }
  }
}

class MongoQueryLocationPoiLayerCounts(database: Database) {
  def execute(locationName: String): Seq[LocationPoiLayerCount] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("location.names", locationName)),
        unwind("$layers"),
        group(
          "$layers",
          sum("count", 1)
        ),
        sort(orderBy(ascending("_id"))),
        project(
          fields(
            excludeId(),
            computed("layer", "$_id"),
            include("count"),
          )
        )
      )
      val layerCounts = database.pois.aggregate[LocationPoiLayerCount](pipeline, log)
      (s"layerCounts: ${layerCounts.size}", layerCounts)
    }
  }
}
