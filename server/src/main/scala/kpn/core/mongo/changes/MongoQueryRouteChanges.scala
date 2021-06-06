package kpn.core.mongo.changes

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.database.doc.RouteChangeDoc
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

object MongoQueryRouteChanges {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "tryout")
      val query = new MongoQueryRouteChanges(database)

      val parameters1 = ChangesParameters(
        routeId = Some(20628L),
        itemsPerPage = 5,
        pageIndex = 0,
        impact = true,
      )
      query.execute(parameters1)
      query.execute(parameters1)

      val parameters2 = ChangesParameters(
        routeId = Some(1599145L),
        itemsPerPage = 5,
        pageIndex = 0,
        impact = true,
      )
      query.execute(parameters2)
      query.execute(parameters2)
      val changes = query.execute(parameters2)
      changes.map(_.key).foreach { key =>
        println(s"${key.timestamp.yyyymmddhhmm}  ${key.replicationNumber}  ${key.changeSetId}")
      }
    }
    finally {
      mongoClient.close()
    }
  }
}

class MongoQueryRouteChanges(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryRouteChanges])

  def execute(parameters: ChangesParameters): Seq[RouteChange] = {

    val timeRange = TimeRange.fromParameters(parameters)

    val filterElements = Seq(
      Seq(equal("routeChange.key.elementId", parameters.routeId.get)),
      if (parameters.impact) {
        Seq(equal("routeChange.impact", true))
      }
      else {
        Seq.empty
      },
      timeRange match {
        case None => Seq.empty
        case Some(range) =>
          Seq(
            gt("routeChange.key.time", range.start),
            lt("routeChange.key.time", range.end),
          )
      }
    ).flatten

    val pipeline: Seq[Bson] = Seq(
      filter(
        and(filterElements: _*)
      ),
      sort(
        orderBy(
          descending(
            "routeChange.key.time",
          )
        )
      ),
      skip((parameters.itemsPerPage * parameters.pageIndex).toInt),
      limit(parameters.itemsPerPage.toInt),
      project(
        fields(
          excludeId()
        )
      )
    )

    // println(Mongo.pipelineString(pipeline))

    log.debugElapsed {
      val collection = database.getCollection("change-routes")
      val future = collection.aggregate[RouteChangeDoc](pipeline).toFuture()
      val docs = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"${docs.size} route changes", docs.map(_.routeChange))
    }
  }
}
