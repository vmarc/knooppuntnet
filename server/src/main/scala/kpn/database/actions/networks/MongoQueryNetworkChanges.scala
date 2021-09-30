package kpn.database.actions.networks

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.database.actions.networks.MongoQueryNetworkChanges.log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryNetworkChanges {

  private val log = Log(classOf[MongoQueryNetworkChanges])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
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
  }
}

class MongoQueryNetworkChanges(database: Database) {

  def execute(networkId: Long, parameters: ChangesParameters): Seq[NetworkInfoChange] = {

    val filterElements = Seq(
      Some(equal("key.elementId", networkId)),
      if (parameters.impact) {
        Some(equal("impact", true))
      }
      else {
        None
      },
      parameters.year.map(year => equal("key.time.year", year.toInt)),
      parameters.month.map(month => equal("key.time.month", month.toInt)),
      parameters.day.map(day => equal("key.time.day", day.toInt))
    ).flatten

    val pipeline: Seq[Bson] = Seq(
      filter(
        and(filterElements: _*)
      ),
      sort(
        orderBy(
          descending(
            "key.time",
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

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val docs = database.networkInfoChanges.aggregate[NetworkInfoChange](pipeline)
      (s"${docs.size} network changes", docs)
    }
  }
}
