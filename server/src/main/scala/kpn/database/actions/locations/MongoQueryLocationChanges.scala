package kpn.database.actions.locations

import kpn.api.common.LocationChangeSet
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.NetworkType
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.locations.MongoQueryLocationChanges.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationFilter
import org.bson.conversions.Bson
import org.mongodb.scala.Document
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Aggregates.count
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
    Mongo.executeIn("kpn-laptop") { database =>
      val parameters = ChangesParameters(
        pageSize = 6,
        impact = false,
        year = None,
        month = None,
        day = None
      )
      val query = new MongoQueryLocationChanges(database)
      val changes = query.execute(LocationFilter(NetworkType.hiking, Seq("nl-1-gd")), parameters)
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
      val locationFilter = LocationFilter(NetworkType.hiking, Seq("nl-1-gd"))
      val count = query.executeCount(locationFilter, parameters)
      println(s"--- total count=$count")
    }
  }
}

class MongoQueryLocationChanges(database: Database) {

  def execute(locationFilter: LocationFilter, parameters: ChangesParameters): Seq[LocationChangeSet] = {

    val pipeline = new PipelineBuilder(locationFilter, parameters).build()

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val changes = database.changes.aggregate[LocationChangeSet](pipeline, allowDiskUse = true)
      (s"${changes.size} location changes", changes)
    }
  }

  def executeCount(locationFilter: LocationFilter, parameters: ChangesParameters): Long = {

    val pipeline = new PipelineBuilder(locationFilter, parameters).buildCountPipeline()

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val countResults = database.changes.aggregate[CountResult](pipeline, allowDiskUse = true)
      val totalCount = countResults.map(_.count).sum
      (s"$totalCount total location changes counted", totalCount)
    }
  }

  def executeFilterOptions(locationFilter: LocationFilter, parameters: ChangesParameters): ChangeSetCounts = {

    val pipeline = new PipelineBuilder(locationFilter, ChangesParameters()).buildFilterOptionsPipeline()

    val yearInt = parameters.year match {
      case None => Time.now.year
      case Some(year) => year.toInt
    }

    val monthInt = parameters.month.map(_.toInt)

    ChangeCountPipeline.execute(
      database.changes,
      pipeline,
      yearInt,
      monthInt,
      log
    )
  }

  private class PipelineBuilder(locationFilter: LocationFilter, parameters: ChangesParameters) {

    def build(): Seq[Bson] = {
      commonStages() ++
        Seq(
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

    def buildCountPipeline(): Seq[Bson] = {
      commonStages() ++
        Seq(
          count()
        )
    }

    def buildFilterOptionsPipeline(): Seq[Bson] = {
      commonStages() ++
        Seq(
          project(
            fields(
              computed("_id", "$_id.id"),
              computed("key", "$_id.key"),
              BsonDocument("""{"impact": {$or: [{$in: [true, "$locationChanges.happy"]}, {$in: [true, "$locationChanges.investigate"]}]}}"""),
              include("locationChanges"),
            )
          )
        )
    }

    private def commonStages(): Seq[Bson] = {
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
      )
    }

    private def mainFilter(): Bson = {
      filter(
        and(
          Seq(
            Some(
              FilterPipeline.locationFieldFilter("locations", locationFilter)
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
            Some(equal("locationChanges.networkType", locationFilter.networkType.name)),
            Some(
              FilterPipeline.locationFieldFilter("locationChanges.locationNames", locationFilter)
            ),
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
