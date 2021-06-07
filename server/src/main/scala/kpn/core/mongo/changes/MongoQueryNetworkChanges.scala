package kpn.core.mongo.changes

import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.database.doc.NetworkChangeDoc
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

object MongoQueryNetworkChanges {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "tryout")
      val query = new MongoQueryNetworkChanges(database)

      query.execute(9532813L, ChangesParameters(impact = true))
      query.execute(9532813L, ChangesParameters(impact = true))
      val changes = query.execute(9532813L, ChangesParameters(impact = true))

      query.execute(9532813L, ChangesParameters())
      query.execute(9532813L, ChangesParameters())

      //      changes.map(_.key).foreach { key =>
      //        println(s"${key.timestamp.yyyymmddhhmm}  ${key.replicationNumber}  ${key.changeSetId}")
      //      }
    }
    finally {
      mongoClient.close()
    }
  }
}

class MongoQueryNetworkChanges(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryNetworkChanges])

  def execute(networkId: Long, parameters: ChangesParameters): Seq[NetworkChange] = {

    val timeRange = TimeRange.fromParameters(parameters)

    val filterElements = Seq(
      Seq(equal("networkChange.networkId", networkId)),
      if (parameters.impact) {
        Seq(equal("networkChange.impact", true))
      }
      else {
        Seq.empty
      },
      timeRange match {
        case None => Seq.empty
        case Some(range) =>
          Seq(
            gt("networkChange.key.time", range.start),
            lt("networkChange.key.time", range.end),
          )
      }
    ).flatten

    //    val filterElements = Seq(
    //      Seq(equal("networkChange.networkId", parameters.networkId.get)),
    //      if (parameters.impact) {
    //        Seq(equal("networkChange.impact", true))
    //      }
    //      else {
    //        //        Seq.empty
    //        Seq(
    //          or( // use impact check (instead of ignoring impact) so that index is used
    //            equal("networkChange.impact", true),
    //            equal("networkChange.impact", false)
    //          )
    //        )
    //      },
    //      range match {
    //        case None => Seq.empty
    //        case Some(r) =>
    //          Seq(
    //            gt("networkChange.key.timestamp", r.start),
    //            lt("networkChange.key.timestamp", r.end),
    //          )
    //      }
    //    ).flatten


    val pipeline: Seq[Bson] = Seq(
      filter(
        and(filterElements: _*)
      ),
      sort(
        orderBy(
          descending(
            "networkChange.key.time",
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
      val collection = database.getCollection("network-changes")
      val future = collection.aggregate[NetworkChangeDoc](pipeline).toFuture()
      val docs = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"${docs.size} network changes", docs.map(_.networkChange))
    }
  }
}
