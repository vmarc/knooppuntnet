package kpn.database.actions.networks

import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.networks.MongoQueryNetworkChangeCounts.log
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

object MongoQueryNetworkChangeCounts {
  private val log = Log(classOf[MongoQueryNetworkChangeCounts])
}

class MongoQueryNetworkChangeCounts(database: Database) {

  def execute(networkId: Long, year: Int, monthOption: Option[Int]): ChangeSetCounts = {

    val pipeline = monthOption match {
      case None =>
        Seq(
          filter(equal("networkId", networkId)),
          facet(
            Facet("years", pipelineYears(networkId): _*),
            Facet("months", pipelineMonths(networkId, year): _*),
          )
        )

      case Some(month) =>
        Seq(
          filter(equal("networkId", networkId)),
          facet(
            Facet("years", pipelineYears(networkId): _*),
            Facet("months", pipelineMonths(networkId, year): _*),
            Facet("days", pipelineDays(networkId, year, month): _*),
          )
        )
    }

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val collection = database.getCollection("network-info-changes")
      val future = collection.aggregate[ChangeSetCounts](pipeline).first().toFuture()
      val counts = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"year: $year, month: ${monthOption.getOrElse('-')}, results: years: ${counts.years.size}, months: ${counts.months.size}, days: ${counts.days.size}"
      (result, counts)
    }
  }

  private def pipelineYears(networkId: Long): Seq[Bson] = {
    ChangeCountPipeline.pipelineYears(
      Some(
        filter(
          equal("networkId", networkId)
        )
      )
    )
  }

  private def pipelineMonths(networkId: Long, year: Int): Seq[Bson] = {
    ChangeCountPipeline.pipelineMonths(
      filter(
        and(
          equal("networkId", networkId),
          equal("key.time.year", year)
        )
      )
    )
  }

  private def pipelineDays(networkId: Long, year: Int, month: Int): Seq[Bson] = {
    ChangeCountPipeline.pipelineDays(
      filter(
        and(
          equal("networkId", networkId),
          equal("key.time.year", year),
          equal("key.time.month", month),
        )
      )
    )
  }
}
