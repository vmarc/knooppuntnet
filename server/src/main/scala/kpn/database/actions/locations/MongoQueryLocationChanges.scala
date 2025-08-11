package kpn.database.actions.locations

import com.mongodb.client.model.Accumulators.push
import com.mongodb.client.model.Aggregates.count
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.skip
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.or
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.LocationChangeSet
import kpn.api.common.RouteType
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.database.actions.base.ChangeCountPipeline
import kpn.database.actions.locations.MongoQueryLocationChanges.log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import org.bson.BsonDocument
import org.bson.Document
import org.bson.conversions.Bson

object MongoQueryLocationChanges {

  private val log = Log(classOf[MongoQueryLocationChanges])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      val parameters = ChangesParameters(
        pageSize = 6,
        year = None,
        month = None,
        day = None
      )
      val query = new MongoQueryLocationChanges(database)
      val subset = LocationSubset("", RouteType.hiking, Seq("nl-1-gd"))
      val changes = query.execute(subset, parameters)
      println("---")
      changes.foreach { change =>
        val timestamp = change.key.timestamp.yyyymmddhhmmss
        val changeSetId = change.key.changeSetId
        println(s"$timestamp $changeSetId, happy=${change.happy}, investigate=${change.investigate}")
        change.locationChanges.foreach { locationChanges =>
          val routeType = locationChanges.routeType.toString
          val location = locationChanges.locationNames.mkString(" > ")
          println(s"  $routeType $location, happy=${locationChanges.happy}, investigate=${locationChanges.investigate}")
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
      val count = query.executeCount(subset, parameters)
      println(s"--- total count=$count")
    }
  }
}

class MongoQueryLocationChanges(database: Database) {

  def execute(subset: LocationSubset, parameters: ChangesParameters): Seq[LocationChangeSet] = {

    val pipeline = new PipelineBuilder(subset, parameters).build()

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val changes = database.changes.aggregate(pipeline, classOf[LocationChangeSet], allowDiskUse = true)
      (s"${changes.size} location changes", changes)
    }
  }

  def executeCount(subset: LocationSubset, parameters: ChangesParameters): Long = {

    val pipeline = new PipelineBuilder(subset, parameters).buildCountPipeline()

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val countResults = database.changes.aggregate(pipeline, classOf[CountResult], allowDiskUse = true)
      val totalCount = countResults.map(_.count).sum
      (s"$totalCount total location changes counted", totalCount)
    }
  }

  def executeFilterOptions(subset: LocationSubset, parameters: ChangesParameters): ChangeSetCounts = {

    val pipeline = new PipelineBuilder(subset, ChangesParameters()).buildFilterOptionsPipeline()

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

  private class PipelineBuilder(subset: LocationSubset, parameters: ChangesParameters) {

    def build(): MongoPipeline = {
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

    def buildCountPipeline(): MongoPipeline = {
      commonStages() ++
        Seq(
          count()
        )
    }

    def buildFilterOptionsPipeline(): MongoPipeline = {
      commonStages() ++
        Seq(
          project(
            fields(
              computed("_id", "$_id.id"),
              computed("key", "$_id.key"),
              BsonDocument.parse("""{"impact": {$or: [{$in: [true, "$locationChanges.happy"]}, {$in: [true, "$locationChanges.investigate"]}]}}"""),
              include("locationChanges"),
            )
          )
        )
    }

    private def commonStages(): MongoPipeline = {
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
          new Document(
            java.util.Map.of(
              "id", "$_id",
              "key", "$key"
            )
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
              LocationQuery.changesLocationFilter("locations", subset)
            ),
            Option.when(parameters.impact) {
              equal("impact", true)
            },
            parameters.year.map(year => equal("key.time.year", year.toInt)),
            parameters.month.map(month => equal("key.time.month", month.toInt)),
            parameters.day.map(day => equal("key.time.day", day.toInt))
          ).flatten *
        )
      )
    }

    private def locationChangesFilter(): Bson = {
      filter(
        and(
          Seq(
            Some(equal("locationChanges.routeType", subset.routeType.toString)),
            Some(
              LocationQuery.changesLocationFilter("locationChanges.locationNames", subset)
            ),
            Option.when(parameters.impact) {
              or(
                equal("locationChanges.happy", true),
                equal("locationChanges.investigate", true),
              )
            }
          ).flatten *
        )
      )
    }
  }
}
