package kpn.core.mongo.actions.nodes

import kpn.core.mongo.actions.nodes.MongoQueryNodeChangeCounts.log
import kpn.core.mongo.actions.nodes.MongoQueryNodeChangeCounts.pipelineDaysString
import kpn.core.mongo.actions.nodes.MongoQueryNodeChangeCounts.pipelineMonthsString
import kpn.core.mongo.actions.nodes.MongoQueryNodeChangeCounts.pipelineYearsString
import kpn.core.mongo.actions.statistics.ChangeSetCounts
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

object MongoQueryNodeChangeCounts extends MongoQuery {
  private val log = Log(classOf[MongoQueryNodeChangeCounts])
  private val pipelineYearsString = readPipelineString("years")
  private val pipelineMonthsString = readPipelineString("months")
  private val pipelineDaysString = readPipelineString("days")
}

class MongoQueryNodeChangeCounts(database: MongoDatabase) extends MongoQuery {

  def execute(nodeId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {

    val pipelineYears = toPipeline(pipelineYearsString.replace("@nodeId", s"$nodeId"))

    val pipelineMonths = toPipeline(
      pipelineMonthsString.
        replace("@nodeId", s"$nodeId").
        replace("@year", s"$year")
    )

    val pipeline = monthOption match {
      case None =>
        Seq(
          filter(equal("key.elementId", nodeId)),
          facet(
            Facet("years", pipelineYears: _*),
            Facet("months", pipelineMonths: _*),
          )
        )

      case Some(month) =>
        val pipelineDays = toPipeline(
          pipelineDaysString.
            replace("@nodeId", s"$nodeId").
            replace("@year", s"$year").
            replace("@month", s"$month")
        )
        Seq(
          filter(equal("key.elementId", nodeId)),
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
      val collection = database.getCollection("node-changes")
      val future = collection.aggregate[ChangeSetCounts](pipeline).first().toFuture()
      val counts = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"year: $year, month: ${monthOption.getOrElse('-')}, results: years: ${counts.years.size}, months: ${counts.months.size}, days: ${counts.days.size}"
      (result, counts)
    }
  }
}
