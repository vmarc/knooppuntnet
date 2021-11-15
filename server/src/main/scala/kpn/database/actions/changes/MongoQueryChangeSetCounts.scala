package kpn.database.actions.changes

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.changes.MongoQueryChangeSetCounts.log
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

object MongoQueryChangeSetCounts {
  private val log = Log(classOf[MongoQueryChangeSetCounts])
}

class MongoQueryChangeSetCounts(database: Database) {

  def execute(year: Int, monthOption: Option[Int]): ChangeSetCounts = {

    val pipeline: Seq[Bson] = monthOption match {
      case None =>
        Seq(
          facet(
            Facet("years", pipelineYears(): _*),
            Facet("months", pipelineMonths(year): _*),
          )
        )

      case Some(month) =>
        Seq(
          facet(
            Facet("years", pipelineYears(): _*),
            Facet("months", pipelineMonths(year): _*),
            Facet("days", pipelineDays(year, month): _*),
          )
        )
    }

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val future = database.changes.native.aggregate[ChangeSetCounts](pipeline).allowDiskUse(true).first().toFuture()
      val counts = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"executeDirectMultiPipeline: years: ${counts.years.size}, months: ${counts.months.size}, days: ${counts.days.size}"
      (result, counts)
    }
  }

  private def pipelineYears(): Seq[Bson] = {
    ChangeCountPipeline.pipelineYears(None)
  }

  private def pipelineMonths(year: Int): Seq[Bson] = {
    ChangeCountPipeline.pipelineMonths(
      filter(equal("key.time.year", year)),
    )
  }

  private def pipelineDays(year: Int, month: Int): Seq[Bson] = {
    ChangeCountPipeline.pipelineDays(
      filter(
        and(
          equal("key.time.year", year),
          equal("key.time.month", month),
        )
      )
    )
  }
}
