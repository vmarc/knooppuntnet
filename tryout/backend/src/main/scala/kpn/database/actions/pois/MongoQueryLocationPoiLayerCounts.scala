package kpn.database.actions.pois

import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.poi.LocationPoiLayerCount
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryLocationPoiLayerCounts.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.util.Mongo

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
      val layerCounts = database.pois.aggregate(pipeline, classOf[LocationPoiLayerCount], log)
      (s"layerCounts: ${layerCounts.size}", layerCounts)
    }
  }
}
