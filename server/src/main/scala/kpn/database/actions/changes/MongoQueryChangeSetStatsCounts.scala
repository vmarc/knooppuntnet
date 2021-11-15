package kpn.database.actions.changes

import kpn.database.actions.changes.MongoQueryChangeSetStatsCounts.log
import kpn.database.actions.changes.MongoQueryChangeSetStatsCounts.pipelineAll
import kpn.database.actions.changes.MongoQueryChangeSetStatsCounts.pipelineDaysString
import kpn.database.actions.changes.MongoQueryChangeSetStatsCounts.pipelineMonthsString
import kpn.database.actions.statistics.ChangeSetCount
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import kpn.database.base.MongoQuery
import kpn.database.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.facet
import org.mongodb.scala.model.Facet

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryChangeSetStatsCounts extends MongoQuery {
  private val log = Log(classOf[MongoQueryChangeSetStatsCounts])
  private val pipelineYears = readPipeline("years").stages
  private val pipelineMonthsString = readPipelineString("months")
  private val pipelineDaysString = readPipelineString("days")
  private val pipelineAll = readPipeline("all").stages
}

// only use MongoQueryChangeSetCounts instead
class MongoQueryChangeSetStatsCounts(database: Database) {

  def execute(year: Int, monthOption: Option[Int]): ChangeSetCounts = {

    val pipelineYears = MongoQueryChangeSetStatsCounts.pipelineYears

    val pipelineMonths = {
      val string = pipelineMonthsString.replace("@year", s"$year")
      new MongoQuery().toPipeline(string)
    }

    val pipeline: Seq[Bson] = monthOption match {
      case None =>
        Seq(
          facet(
            Facet("years", pipelineYears: _*),
            Facet("months", pipelineMonths: _*),
          )
        )

      case Some(month) =>
        val pipelineDays = {
          val string = pipelineDaysString.replace("@year", s"$year").replace("@month", s"$month")
          new MongoQuery().toPipeline(string)
        }
        Seq(
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
      val collection = database.getCollection("change-stats-summaries")
      val future = collection.aggregate[ChangeSetCounts](pipeline).first().toFuture()
      val counts = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"year: $year, month: ${monthOption.getOrElse('-')}, results: years: ${counts.years.size}, months: ${counts.months.size}, days: ${counts.days.size}"
      (result, counts)
    }
  }

  def allDays(): Seq[ChangeSetCount] = {
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipelineAll))
    }
    log.debugElapsed {
      val collection = database.getCollection("change-stats-summaries")
      val future = collection.aggregate[ChangeSetCount](pipelineAll).toFuture()
      val counts = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"all days materialized ${counts.size} counts", counts)
    }
  }
}
