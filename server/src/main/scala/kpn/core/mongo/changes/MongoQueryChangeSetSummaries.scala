package kpn.core.mongo.changes

import kpn.api.common.ChangeSetSummary
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.database.doc.ChangeSetSummaryDoc
import kpn.core.mongo.changes.MongoQueryChangeSetSummaries.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.gt
import org.mongodb.scala.model.Filters.lt
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryChangeSetSummaries {

  private val log = Log(classOf[MongoQueryChangeSetSummaries])

  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val parameters = ChangesParameters(
        itemsPerPage = 200,
        year = Some("2017")
      )
      val database = Mongo.database(mongoClient, "tryout")
      val query = new MongoQueryChangeSetSummaries(database)
      query.execute(parameters)
      val summaries = query.execute(parameters)
      summaries.map(_.key).foreach(println)
    }
    finally {
      mongoClient.close()
    }
  }
}

class MongoQueryChangeSetSummaries(database: MongoDatabase) {

  def execute(parameters: ChangesParameters): Seq[ChangeSetSummary] = {

    val range: Option[Range] = parameters.year match {
      case None => None
      case Some(year) =>
        parameters.month match {
          case None =>
            Some(
              Range(
                s"$year-00-00T00:00:00Z",
                s"$year-99-99T99:99:99Z"
              )
            )
          case Some(month) =>
            parameters.day match {
              case None =>
                Some(
                  Range(
                    s"$year-$month-00T00:00:00Z",
                    s"$year-$month-99T99:99:99Z"
                  )
                )
              case day =>
                Some(
                  Range(
                    s"$year-$month-${day}T00:00:00Z",
                    s"$year-$month-${day}T99:99:99Z"
                  )
                )
            }
        }
    }

    val pipeline: Seq[Bson] = Seq(
      range match {
        case None => null
        case Some(r) =>
          filter(
            and(
              gt("changeSetSummary.key.timestamp", r.start),
              lt("changeSetSummary.key.timestamp", r.end),
            )
          )
      },
      if (parameters.impact) {
        filter(
          equal("changeSetSummary.impact", true),
        )
      }
      else {
        null
      },
      sort(orderBy(descending("changeSetSummary.key.timestamp"))),
      skip((parameters.itemsPerPage * parameters.pageIndex).toInt),
      limit(parameters.itemsPerPage.toInt),
      project(
        fields(
          excludeId()
        )
      )
    ).filterNot(_ == null)

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val collection = database.getCollection("changeset-summaries")
      val future = collection.aggregate[ChangeSetSummaryDoc](pipeline).toFuture()
      val docs = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val summaries = docs.map(_.changeSetSummary)
      (s"${summaries.size} changeset summaries", summaries)
    }
  }
}
