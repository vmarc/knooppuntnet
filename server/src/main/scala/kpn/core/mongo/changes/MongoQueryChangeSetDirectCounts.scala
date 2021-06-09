package kpn.core.mongo.changes

import kpn.core.mongo.changes.MongoQueryChangeSetDirectCounts.log
import kpn.core.mongo.changes.MongoQueryChangeSetDirectCounts.pipelineAll
import kpn.core.mongo.changes.MongoQueryChangeSetDirectCounts.pipelineDaysString
import kpn.core.mongo.changes.MongoQueryChangeSetDirectCounts.pipelineMonthsString
import kpn.core.mongo.changes.MongoQueryChangeSetDirectCounts.pipelineYears
import kpn.core.mongo.statistics.ChangeSetCount
import kpn.core.mongo.statistics.ChangeSetCounts
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.facet
import org.mongodb.scala.model.Facet

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryChangeSetDirectCounts extends MongoQuery {
  private val log = Log(classOf[MongoQueryChangeSetDirectCounts])
  private val pipelineYears = readPipeline("years").stages
  private val pipelineMonthsString = readPipelineString("months")
  private val pipelineDaysString = readPipelineString("days")
  private val pipelineAll = readPipeline("all").stages
}

class MongoQueryChangeSetDirectCounts(database: MongoDatabase) {

  def allDays(): Seq[ChangeSetCount] = {
    log.debugElapsed {
      val collection = database.getCollection("changeset-summaries")
      val future = collection.aggregate[ChangeSetCount](pipelineAll).toFuture()
      val counts = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val sortedCounts = counts.sortBy(c => (c.year, c.month, c.day, c.impact))
      (s"all days direct ${counts.size} counts", sortedCounts)
    }
  }

  def execute(year: Int, monthOption: Option[Int]): ChangeSetCounts = {

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
      val collection = database.getCollection("changeset-summaries")
      val future = collection.aggregate[ChangeSetCounts](pipeline).allowDiskUse(true).first().toFuture()
      val counts = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"executeDirectMultiPipeline: years: ${counts.years.size}, months: ${counts.months.size}, days: ${counts.days.size}"
      (result, counts)
    }
  }
}
