package kpn.database.actions.locations

import kpn.api.common.LocationChangeSet
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.database.actions.locations.MongoQueryLocationChanges.log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.Document
import org.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.or
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryLocationChanges {

  private val log = Log(classOf[MongoQueryLocationChanges])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-3") { database =>
      val parameters = ChangesParameters(
        pageSize = 6,
        impact = true,
        year = Some(2022),
        month = Some(2),
        day = Some(28)
      )
      val query = new MongoQueryLocationChanges(database)
      val changes = query.execute(NetworkType.hiking, "fr", parameters)
      println("---")
      changes.foreach { change =>
        val timestamp = change.key.timestamp.yyyymmddhhmmss
        val changeSetId = change.key.changeSetId
        println(s"$timestamp $changeSetId, happy=${change.happy}, investigate=${change.investigate}")
        change.locationChanges.foreach { locationChanges =>
          val networkType = locationChanges.networkType.name
          val location = locationChanges.locationNames.mkString(" > ")
          println(s"  $networkType $location, happy=${locationChanges.happy}, investigate=${locationChanges.investigate}")
          locationChanges.nodeChanges.removed.foreach { ref =>
            println(s"    node removed: ${ref.name}, happy=${ref.happy}, investigate=${ref.investigate}")
          }
          locationChanges.nodeChanges.added.foreach { ref =>
            println(s"    node added: ${ref.name}, happy=${ref.happy}, investigate=${ref.investigate}")
          }
          locationChanges.nodeChanges.updated.foreach { ref =>
            println(s"    node updated: ${ref.name}, happy=${ref.happy}, investigate=${ref.investigate}")
          }

          locationChanges.routeChanges.removed.foreach { ref =>
            println(s"    route removed: ${ref.name}, happy=${ref.happy}, investigate=${ref.investigate}")
          }
          locationChanges.routeChanges.added.foreach { ref =>
            println(s"    route added: ${ref.name}, happy=${ref.happy}, investigate=${ref.investigate}")
          }
          locationChanges.routeChanges.updated.foreach { ref =>
            println(s"    route updated: ${ref.name}, happy=${ref.happy}, investigate=${ref.investigate}")
          }
        }
      }
      println("---")
    }
  }
}

class MongoQueryLocationChanges(database: Database) {

  def execute(networkType: NetworkType, locationName: String, parameters: ChangesParameters): Seq[LocationChangeSet] = {

    val pipeline = new PipelineBuilder(networkType, locationName, parameters).build()

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val changes = database.changes.aggregate[LocationChangeSet](pipeline, allowDiskUse = true)
      (s"${changes.size} location changes", changes)
    }
  }

  private class PipelineBuilder(networkType: NetworkType, locationName: String, parameters: ChangesParameters) {

    def build(): Seq[Bson] = {
      Seq(
        mainFilter(),
        project(
          fields(
            include("key"),
            include("locationChanges"),
          )
        ),
        unwind("$locationChanges"),
        locationChangesFilter(),
        group(
          Document(
            "id" -> "$_id",
            "key" -> "$key"
          ),
          push("locationChanges", "$locationChanges")
        ),
        sort(orderBy(descending("_id.key.time"))),
        skip((parameters.pageSize * parameters.pageIndex).toInt),
        limit(parameters.pageSize.toInt),
        project(
          fields(
            computed("_id", "$_id.id"),
            computed("key", "$_id.key"),
            include("locationChanges"),
          )
        )
      )
    }

    private def mainFilter(): Bson = {
      filter(
        and(
          Seq(
            Some(
              equal("locations", locationName)
            ),
            if (parameters.impact) {
              Some(equal("impact", true))
            }
            else {
              None
            },
            parameters.year.map(year => equal("key.time.year", year.toInt)),
            parameters.month.map(month => equal("key.time.month", month.toInt)),
            parameters.day.map(day => equal("key.time.day", day.toInt))
          ).flatten: _*
        )
      )
    }

    private def locationChangesFilter(): Bson = {
      filter(
        and(
          Seq(
            Some(equal("locationChanges.networkType", networkType.name)),
            Some(equal("locationChanges.locationNames", locationName)),
            if (parameters.impact) {
              Some(
                or(
                  equal("locationChanges.happy", true),
                  equal("locationChanges.investigate", true),
                )
              )
            }
            else {
              None
            }
          ).flatten: _*
        )
      )
    }
  }
}
