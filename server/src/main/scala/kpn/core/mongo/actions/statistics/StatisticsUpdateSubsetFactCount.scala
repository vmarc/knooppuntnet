package kpn.core.mongo.actions.statistics

import kpn.api.common.statistics.StatisticValue
import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetFactCount.log
import kpn.core.mongo.util.MongoProjections.concat
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.merge
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unionWith
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.MergeOptions
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

object StatisticsUpdateSubsetFactCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetFactCount])
}

class StatisticsUpdateSubsetFactCount(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        networkFactCountPipeline(),
        Seq(unionWith(database.nodes.name, nodeFactCountPipeline(): _*)),
        Seq(unionWith(database.routes.name, routeFactCountPipeline(): _*)),
        combineFactCounts()
      ).flatten
      val values = database.networkInfos.aggregate[StatisticValue](pipeline)
      (s"${values.size} values", ())
    }
  }

  private def networkFactCountPipeline(): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("active", true),
          exists("country")
        )
      ),
      unwind("$facts"),
      filter(
        /*
           Only include the network level facts, not the node and route facts
           that are alse included in NetworkInfoDoc.
        */
        exists("facts.elementType", exists = false)
      ),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$summary.networkType"
        ),
        sum("factCount", 1)
      )
    )
  }

  private def routeFactCountPipeline(): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("labels", "active"),
          exists("summary.country")
        )
      ),
      unwind("$facts"),
      group(
        Document(
          "country" -> "$summary.country",
          "networkType" -> "$summary.networkType"
        ),
        sum("factCount", 1)
      )
    )
  }

  private def nodeFactCountPipeline(): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("labels", "active")
        )
      ),
      unwind("$names"),
      unwind("$facts"),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$names.networkType"
        ),
        sum("factCount", 1)
      )
    )
  }

  private def combineFactCounts(): Seq[Bson] = {
    Seq(
      group(
        "$_id",
        sum("factCount", "$factCount")
      ),
      project(
        fields(
          concat("_id", "$_id.country", ":", "$_id.networkType", ":FactCount"),
          computed("country", "$_id.country"),
          computed("networkType", "$_id.networkType"),
          computed("name", "FactCount"),
          computed("value", "$factCount")
        )
      ),
      merge(
        database.statisticsSubsetFactCount.name,
        new MergeOptions().uniqueIdentifier("_id")
      )
    )
  }
}
