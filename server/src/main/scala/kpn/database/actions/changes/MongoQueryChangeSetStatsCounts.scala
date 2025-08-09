package kpn.database.actions.changes

import com.mongodb.client.model.Aggregates.facet
import kpn.core.util.Log
import kpn.core.util.Util.seqToList
import kpn.database.actions.changes.MongoQueryChangeSetStatsCounts.log
import kpn.database.actions.changes.MongoQueryChangeSetStatsCounts.pipelineAll
import kpn.database.actions.changes.MongoQueryChangeSetStatsCounts.pipelineDaysString
import kpn.database.actions.changes.MongoQueryChangeSetStatsCounts.pipelineMonthsString
import kpn.database.actions.statistics.ChangeSetCount
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.ffacet
import kpn.database.base.MongoQuery
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo

import scala.jdk.CollectionConverters.IterableHasAsScala

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

    val pipeline: MongoPipeline = monthOption match {
      case None =>
        Seq(
          facet(
            ffacet("years", pipelineYears),
            ffacet("months", pipelineMonths),
          )
        )

      case Some(month) =>
        val pipelineDays = {
          val string = pipelineDaysString.replace("@year", s"$year").replace("@month", s"$month")
          new MongoQuery().toPipeline(string)
        }
        Seq(
          facet(
            ffacet("years", pipelineYears),
            ffacet("months", pipelineMonths),
            ffacet("days", pipelineDays),
          )
        )
    }

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val collection = database.getCollection("change-stats-summaries")
      val counts = collection.aggregate(seqToList(pipeline), classOf[ChangeSetCounts]).first()
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
      val counts = collection.aggregate(seqToList(pipelineAll), classOf[ChangeSetCount]).asScala.toSeq
      (s"all days materialized ${counts.size} counts", counts)
    }
  }
}
