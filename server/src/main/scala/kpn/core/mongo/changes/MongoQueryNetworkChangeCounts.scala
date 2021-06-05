package kpn.core.mongo.changes

import kpn.core.mongo.changes.MongoQueryNetworkChangeCounts.pipelineAllString
import kpn.core.mongo.changes.MongoQueryNetworkChangeCounts.pipelineDaysString
import kpn.core.mongo.changes.MongoQueryNetworkChangeCounts.pipelineMonthsString
import kpn.core.mongo.changes.MongoQueryNetworkChangeCounts.pipelineYearsString
import kpn.core.mongo.statistics.ChangeSetCounts
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.facet
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Facet
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNetworkChangeCounts extends MongoQuery {
  private val pipelineYearsString = readPipelineString("years")
  private val pipelineMonthsString = readPipelineString("months")
  private val pipelineDaysString = readPipelineString("days")
}

class MongoQueryNetworkChangeCounts(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryNetworkChangeCounts])

  def execute(networkId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {

    val pipelineYears = {
      val string = pipelineYearsString.
        replace("@networkId", s"$networkId")
      new MongoQuery().toPipeline(string)
    }

    val pipelineMonths = {
      val string = pipelineMonthsString.
        replace("@networkId", s"$networkId").
        replace("@year", s"$year")
      new MongoQuery().toPipeline(string)
    }

    val pipeline: Seq[Bson] = monthOption match {
      case None =>
        Seq(
          filter(equal("networkChange.networkId", networkId)),
          facet(
            Facet("years", pipelineYears: _*),
            Facet("months", pipelineMonths: _*),
          )
        )

      case Some(month) =>
        val pipelineDays = {
          val string = pipelineDaysString.
            replace("@networkId", s"$networkId").
            replace("@year", s"$year").
            replace("@month", s"$month")
          new MongoQuery().toPipeline(string)
        }
        Seq(
          filter(equal("networkChange.networkId", networkId)),
          facet(
            Facet("years", pipelineYears: _*),
            Facet("months", pipelineMonths: _*),
            Facet("days", pipelineDays: _*),
          )
        )
    }

    // println(Mongo.pipelineString(pipeline))

    log.debugElapsed {
      val collection = database.getCollection("change-networks")
      val future = collection.aggregate[ChangeSetCounts](pipeline).first().toFuture()
      val counts = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"year: $year, month: ${monthOption.getOrElse('-')}, results: years: ${counts.years.size}, months: ${counts.months.size}, days: ${counts.days.size}"
      (result, counts)
    }
  }
}
