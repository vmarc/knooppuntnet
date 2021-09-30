package kpn.database.actions.routes

import kpn.database.actions.routes.MongoQueryRouteChangeCounts.log
import kpn.database.actions.routes.MongoQueryRouteChangeCounts.pipelineDaysString
import kpn.database.actions.routes.MongoQueryRouteChangeCounts.pipelineMonthsString
import kpn.database.actions.routes.MongoQueryRouteChangeCounts.pipelineYearsString
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import kpn.database.base.MongoQuery
import kpn.database.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.facet
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Facet
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryRouteChangeCounts extends MongoQuery {
  private val log = Log(classOf[MongoQueryRouteChangeCounts])
  private val pipelineYearsString = readPipelineString("years")
  private val pipelineMonthsString = readPipelineString("months")
  private val pipelineDaysString = readPipelineString("days")
}

class MongoQueryRouteChangeCounts(database: Database) extends MongoQuery {

  def execute(routeId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {

    val pipelineYears = toPipeline(pipelineYearsString.replace("@routeId", s"$routeId"))

    val pipelineMonths = toPipeline(
      pipelineMonthsString.
        replace("@routeId", s"$routeId").
        replace("@year", s"$year")
    )

    val pipeline = monthOption match {
      case None =>
        Seq(
          filter(equal("key.elementId", routeId)),
          facet(
            Facet("years", pipelineYears: _*),
            Facet("months", pipelineMonths: _*),
          )
        )

      case Some(month) =>
        val pipelineDays = toPipeline(
          pipelineDaysString.
            replace("@routeId", s"$routeId").
            replace("@year", s"$year").
            replace("@month", s"$month")
        )
        Seq(
          filter(equal("key.elementId", routeId)),
          facet(
            Facet("years", pipelineYears: _*),
            Facet("months", pipelineMonths: _*),
            Facet("days", pipelineDays: _*),
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
}
