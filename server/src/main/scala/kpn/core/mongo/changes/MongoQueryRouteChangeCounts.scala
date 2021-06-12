package kpn.core.mongo.changes

import kpn.core.mongo.changes.MongoQueryRouteChangeCounts.log
import kpn.core.mongo.changes.MongoQueryRouteChangeCounts.pipelineDaysString
import kpn.core.mongo.changes.MongoQueryRouteChangeCounts.pipelineMonthsString
import kpn.core.mongo.changes.MongoQueryRouteChangeCounts.pipelineYearsString
import kpn.core.mongo.statistics.ChangeSetCounts
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
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

class MongoQueryRouteChangeCounts(database: MongoDatabase) extends MongoQuery {

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
