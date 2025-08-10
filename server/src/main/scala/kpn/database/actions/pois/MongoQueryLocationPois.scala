package kpn.database.actions.pois

import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.skip
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.poi.LocationPoiInfo
import kpn.api.common.poi.LocationPoiParameters
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryLocationPois.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.util.Mongo

object MongoQueryLocationPois {
  private val log = Log(classOf[MongoQueryLocationPois])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      val result = new MongoQueryLocationPois(database).execute("be-2-11016", LocationPoiParameters(500), Seq("pub"))
      result.foreach(println)
    }
  }
}

class MongoQueryLocationPois(database: Database) {
  def execute(locationName: String, parameters: LocationPoiParameters, layers: Seq[String]): Seq[LocationPoiInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            Seq(
              Some(equal("location.names", locationName)),
              LayerFilter.of(layers)
            ).flatten *
          )
        ),
        sort(orderBy(ascending("layers.0", "description"))),
        skip((parameters.pageSize * parameters.pageIndex).toInt),
        limit(parameters.pageSize.toInt),
        project(
          fields(
            computed("rowIndex", "0"),
            include("elementType"),
            include("elementId"),
            include("layers"),
            include("description"),
            include("address"),
            include("link"),
            include("image"),
          )
        )
      )

      val locationPoiInfos = database.pois.aggregate(pipeline, classOf[LocationPoiInfo], log).zipWithIndex.map { case (info, index) =>
        val rowIndex = parameters.pageSize * parameters.pageIndex + index
        info.copy( // could have done this in the aggregation?
          rowIndex = rowIndex
        )
      }
      (s"locationPoiInfos: ${locationPoiInfos.size}", locationPoiInfos)
    }
  }
}
