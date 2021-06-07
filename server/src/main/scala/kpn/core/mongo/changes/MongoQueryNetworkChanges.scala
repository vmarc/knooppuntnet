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

      val parameters1 = ChangesParameters(
        networkId = Some(9532813),
        itemsPerPage = 5,
        pageIndex = 0,
        impact = true,
      )
      query.execute(parameters1)
      query.execute(parameters1)
      val changes = query.execute(parameters1)


      val parameters2 = ChangesParameters(
        networkId = Some(9532813),
        itemsPerPage = 5,
        pageIndex = 0,
      )
      query.execute(parameters2)
      query.execute(parameters2)


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

  def execute(parameters: ChangesParameters): Seq[NetworkChange] = {

    val timeRange: Option[TimeRange] = parameters.year match {
      case None => None
      case Some(year) =>
        parameters.month match {
          case None =>
            Some(
              TimeRange(
                Seq(year.toLong, 0),
                Seq(year.toLong, 99),
              )
            )
          case Some(month) =>
            parameters.day match {
              case None =>
                Some(
                  TimeRange(
                    Seq(year.toLong, month.toLong, 0),
                    Seq(year.toLong, month.toLong, 99),
                  )
                )
              case Some(day) =>
                Some(
                  TimeRange(
                    Seq(year.toLong, month.toLong, day.toLong, 0),
                    Seq(year.toLong, month.toLong, day.toLong, 99),
                  )
                )
            }
        }
    }

    val filterElements = Seq(
      Seq(equal("networkChange.networkId", parameters.networkId.get)),
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
