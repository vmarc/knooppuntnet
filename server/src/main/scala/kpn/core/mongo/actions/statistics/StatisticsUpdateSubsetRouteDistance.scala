package kpn.core.mongo.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.MongoQueryStatistics.groupValues
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteDistance.log
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object StatisticsUpdateSubsetRouteDistance extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetRouteDistance])
}

class StatisticsUpdateSubsetRouteDistance(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val select = Seq(
        filter(
          and(
            equal("labels", "active"),
            exists("summary.country")
          )
        ),
        group(
          Document(
            "country" -> "$summary.country",
            "networkType" -> "$summary.networkType"
          ),
          sum("value", "$summary.meters")
        ),
        project(
          fields(
            include("_id"),
            computed("value", BsonDocument("""{$divide: ["$value", 1000]}"""))
          )
        )
      )
      val pipeline = select ++ groupValues(database, "Distance")
      val values = database.routes.aggregate[StatisticValues](pipeline)
      (s"${values.size} values", ())
    }
  }
}
