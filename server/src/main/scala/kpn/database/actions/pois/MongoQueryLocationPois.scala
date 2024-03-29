package kpn.database.actions.pois

import kpn.api.common.poi.LocationPoiInfo
import kpn.api.common.poi.LocationPoiParameters
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryLocationPois.log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

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
            ).flatten: _*
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

      val locationPoiInfos = database.pois.aggregate[LocationPoiInfo](pipeline, log).zipWithIndex.map { case (info, index) =>
        val rowIndex = parameters.pageSize * parameters.pageIndex + index
        info.copy( // could have done this in the aggregation?
          rowIndex = rowIndex
        )
      }
      (s"locationPoiInfos: ${locationPoiInfos.size}", locationPoiInfos)
    }
  }
}
