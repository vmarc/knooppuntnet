package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.routes.MongoQueryRouteChangeCounts.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.facet
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Facet
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryRouteChangeCounts {
  private val log = Log(classOf[MongoQueryRouteChangeCounts])
}

class MongoQueryRouteChangeCounts(database: Database) {

  def execute(routeId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {

    val pipeline = monthOption match {
      case None =>
        Seq(
          filter(equal("key.elementId", routeId)),
          facet(
            Facet("years", pipelineYears(routeId): _*),
            Facet("months", pipelineMonths(routeId, year): _*),
          )
        )

      case Some(month) =>
        Seq(
          filter(equal("key.elementId", routeId)),
          facet(
            Facet("years", pipelineYears(routeId): _*),
            Facet("months", pipelineMonths(routeId, year): _*),
            Facet("days", pipelineDays(routeId, year, month): _*),
          )
        )
    }

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val collection = database.getCollection("route-changes")
      val future = collection.aggregate[ChangeSetCounts](pipeline).first().toFuture()
      val counts = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"year: $year, month: ${monthOption.getOrElse('-')}, results: years: ${counts.years.size}, months: ${counts.months.size}, days: ${counts.days.size}"
      (result, counts)
    }
  }

  private def pipelineYears(routeId: Long): Seq[Bson] = {
    ChangeCountPipeline.pipelineYears(
      Some(
        filter(
          equal("key.elementId", routeId)
        )
      )
    )
  }

  private def pipelineMonths(routeId: Long, year: Int): Seq[Bson] = {
    ChangeCountPipeline.pipelineMonths(
      filter(
        and(
          equal("key.elementId", routeId),
          equal("key.time.year", year)
        )
      )
    )
  }

  private def pipelineDays(routeId: Long, year: Int, month: Int): Seq[Bson] = {
    ChangeCountPipeline.pipelineDays(
      filter(
        and(
          equal("key.elementId", routeId),
          equal("key.time.year", year),
          equal("key.time.month", month)
        )
      )
    )
  }
}
