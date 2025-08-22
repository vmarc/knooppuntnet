package kpn.server.analyzer.engine.analysis.post

import com.mongodb.client.model.Accumulators.push
import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.out
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.exists
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Filters.not
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.Log
import kpn.database.actions.statistics.StatisticLongValues
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.arrayEmpty
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoAggregates.notEqual
import kpn.database.base.MongoAggregates.unionWith
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.MongoProjections.concat
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import org.bson.BsonDocument
import org.bson.Document
import org.bson.conversions.Bson
import org.springframework.stereotype.Component

object StatisticsUpdater {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      new StatisticsUpdater(database).execute()
    }
  }
}

@Component
class StatisticsUpdater(database: Database) {

  private val log = Log(classOf[StatisticsUpdater])

  def execute(): Unit = {

    log.debugElapsed {
      val pipeline =
        pipelineNodeCount() ++
          Seq(
            unionWith(database.nodes.name, pipelineOrphanNodeCount()),
            unionWith(database.routes.name, pipelineRouteCount()),
            unionWith(database.orphanRoutes.name, pipelineOrphanRouteCount()),
            unionWith(database.nodes.name, pipelineNodeFacts()),
            unionWith(database.nodes.name, pipelineNodeIntegrityCheckCount()),
            unionWith(database.nodes.name, pipelineNodeIntegrityCheckFailedCount()),
            unionWith(database.routes.name, pipelineRouteFacts()),
            unionWith(database.routes.name, pipelineRouteDistance()),
            unionWith(database.networks.name, pipelineNetworkCount()),
            unionWith(database.networks.name, pipelineNetworkFacts()),
            unionWith(database.networks.name, pipelineNetworkFacts2()),
            unionWith(database.networks.name, pipelineNetworkFacts3()),
            unionWith(database.networks.name, factCountPipeline()),
            unionWith(database.networks.name, pipelineIntegrityCheckNetworkCount()),
            unionWith(database.changes.name, pipelineChangeCount()),
            out(database.statistics.name)
          )

      val values = database.nodes.aggregate(pipeline, classOf[StatisticLongValues])
      (s"${values.size} values", ())
    }
  }

  private def pipelineNodeCount(): MongoPipeline = {
    factPipeline(
      "NodeCount",
      filter(
        and(
          equal("active", true),
          exists("country")
        )
      ),
      unwind("$names"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$names.routeType"
          )
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineOrphanNodeCount(): MongoPipeline = {
    factPipeline(
      "OrphanNodeCount",
      filter(
        and(
          equal("active", true),
          arrayEmpty("routeReferences"),
          arrayEmpty("networkRelationReferences"),
        )
      ),
      unwind("$names"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$names.routeType"
          )
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineRouteCount(): MongoPipeline = {
    factPipeline(
      "RouteCount",
      filter(
        and(
          equal("active", true),
          exists("summary.countries.0")
        )
      ),
      unwind("$summary.countries"),
      unwind("$summary.routeTypes"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$summary.countries",
            "routeType", "$summary.routeTypes"
          )
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineOrphanRouteCount(): MongoPipeline = {
    factPipeline(
      "OrphanRouteCount",
      unwind("$routeTypes"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$routeTypes"
          )
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineNodeFacts(): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("facts")
        )
      ),
      unwind("$names"),
      unwind("$facts"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$names.routeType",
            "factName", "$facts"
          )
        ),
        sum("value", 1)
      ),
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          concat("factName", "$_id.factName", "Count"),
          computed(
            "values",
            fields(
              computed("country", "$_id.country"),
              computed("routeType", "$_id.routeType"),
              computed("value", "$value")
            )
          )
        )
      ),
      group(
        "$factName",
        push("values", "$values")
      )
    )
  }

  private def pipelineRouteFacts(): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          exists("summary.countries.0"),
          exists("facts")
        )
      ),
      unwind("$facts"),
      unwind("$summary.countries"),
      unwind("$summary.routeTypes"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$summary.countries",
            "routeType", "$summary.routeTypes",
            "factName", "$facts"
          )
        ),
        sum("value", 1)
      ),
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          concat("factName", "$_id.factName", "Count"),
          computed(
            "values",
            fields(
              computed("country", "$_id.country"),
              computed("routeType", "$_id.routeType"),
              computed("value", "$value")
            )
          )
        )
      ),
      group(
        "$factName",
        push("values", "$values")
      )
    )
  }

  private def pipelineNetworkFacts(): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.routeType"),
          exists("facts")
        )
      ),
      unwind("$facts"),
      filter(
        in(
          "facts.fact",
          "NetworkExtraMemberNode",
          "NetworkExtraMemberWay",
          "NetworkExtraMemberRelation",
        )
      ),
      project(
        fields(
          computed("factName", "$facts.fact"),
          include("country"),
          computed("routeType", "$summary.routeType"),
          arraySize("factCount", "$facts.elementIds")
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$routeType",
            "factName", "$factName"
          )
        ),
        sum("value", "$factCount")
      ),
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          concat("factName", "$_id.factName", "Count"),
          computed(
            "values",
            fields(
              computed("country", "$_id.country"),
              computed("routeType", "$_id.routeType"),
              computed("value", "$value")
            )
          )
        )
      ),
      group(
        "$factName",
        push("values", "$values")
      )
    )
  }

  private def pipelineNetworkFacts2(): MongoPipeline = {

    Seq(
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.routeType"),
          exists("facts")
        )
      ),
      unwind("$facts"),
      filter(
        in(
          "facts.fact",
          "NodeMemberMissing",
        )
      ),
      project(
        fields(
          computed("factName", "$facts.fact"),
          include("country"),
          computed("routeType", "$summary.routeType"),
          arraySize("factCount", "$facts.elements")
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$routeType",
            "factName", "$factName"
          )
        ),
        sum("value", "$factCount")
      ),
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          concat("factName", "$_id.factName", "Count"),
          computed(
            "values",
            fields(
              computed("country", "$_id.country"),
              computed("routeType", "$_id.routeType"),
              computed("value", "$value")
            )
          )
        )
      ),
      group(
        "$factName",
        push("values", "$values")
      )
    )
  }

  private def pipelineNetworkFacts3(): MongoPipeline = {

    Seq(
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.routeType"),
          exists("facts")
        )
      ),
      unwind("$facts"),
      filter(
        in(
          "facts.fact",
          "NameMissing",
        )
      ),
      project(
        fields(
          computed("factName", "$facts.fact"),
          include("country"),
          computed("routeType", "$summary.routeType"),
          computed("factCount", 1)
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$routeType",
            "factName", "$factName"
          )
        ),
        sum("value", "$factCount")
      ),
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          concat("factName", "$_id.factName", "Count"),
          computed(
            "values",
            fields(
              computed("country", "$_id.country"),
              computed("routeType", "$_id.routeType"),
              computed("value", "$value")
            )
          )
        )
      ),
      group(
        "$factName",
        push("values", "$values")
      )
    )
  }

  private def pipelineRouteDistance(): MongoPipeline = {
    factPipeline(
      "Distance",
      filter(
        and(
          equal("active", true),
          exists("summary.countries.0")
        )
      ),
      unwind("$summary.countries"),
      unwind("$summary.routeTypes"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$summary.countries",
            "routeType", "$summary.routeTypes"
          )
        ),
        sum("value", "$summary.meters")
      ),
      project(
        fields(
          include("_id"),
          computed("value", BsonDocument.parse("""{$toLong: {$divide: ["$value", 1000]}}"""))
        )
      )
    )
  }

  private def pipelineNetworkCount(): MongoPipeline = {
    factPipeline(
      "NetworkCount",
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.routeType")
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$summary.routeType"
          )
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineIntegrityCheckNetworkCount(): MongoPipeline = {
    factPipeline(
      "IntegrityCheckNetworkCount",
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.routeType"),
          equal("detail.integrity.hasChecks", true),
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$summary.routeType"
          )
        ),
        sum("value", 1)
      )
    )
  }

  private def factCountPipeline(): MongoPipeline = {
    networkFactCountPipeline() ++
      Seq(unionWith(database.nodes.name, nodeFactCountPipeline())) ++
      Seq(unionWith(database.routes.name, routeFactCountPipeline())) ++
      combineFactCounts()
  }

  private def combineFactCounts(): MongoPipeline = {
    Seq(
      group(
        "$_id",
        sum("factCount", "$factCount")
      ),
      project(
        fields(
          computed("country", "$_id.country"),
          computed("routeType", "$_id.routeType"),
          computed("value", "$factCount")
        )
      ),
      sort(orderBy(ascending("_id"))),
      project(
        fields(
          computed(
            "values",
            fields(
              computed("country", "$_id.country"),
              computed("routeType", "$_id.routeType"),
              computed("value", "$value")
            )
          )
        )
      ),
      group(
        "FactCount",
        push("values", "$values")
      )
    )
  }

  private def networkFactCountPipeline(): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          exists("country")
        )
      ),
      unwind("$facts"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$summary.routeType"
          )
        ),
        sum("factCount", 1)
      )
    )
  }

  private def routeFactCountPipeline(): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          exists("summary.countries.0")
        )
      ),
      unwind("$facts"),
      unwind("$summary.countries"),
      unwind("$summary.routeTypes"),
      filter(
        and(
          notEqual("facts", "RouteBroken"),
          notEqual("facts", "RouteNotForward"),
          notEqual("facts", "RouteNotBackward"),
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "country", "$summary.countries",
            "routeType", "$summary.routeTypes"
          )
        ),
        sum("factCount", 1)
      )
    )
  }

  private def nodeFactCountPipeline(): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
        )
      ),
      unwind("$names"),
      unwind("$facts"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$names.routeType"
          )
        ),
        sum("factCount", 1)
      )
    )
  }

  private def pipelineChangeCount(): MongoPipeline = {
    factPipeline(
      "ChangeCount",
      unwind("$subsets"),
      group(
        new Document(
          java.util.Map.of(
            "country", "$subsets.country",
            "routeType", "$subsets.routeType"
          )
        ),
        sum("value", 1)
      )
    )
  }

  private def factPipeline(name: String, aggregateElements: Bson*): MongoPipeline = {
    aggregateElements ++
      Seq(
        sort(orderBy(ascending("_id"))),
        project(
          fields(
            computed(
              "values",
              fields(
                computed("country", "$_id.country"),
                computed("routeType", "$_id.routeType"),
                computed("value", "$value")
              )
            )
          )
        ),
        group(
          name,
          push("values", "$values")
        )
      )
  }

  private def pipelineNodeIntegrityCheckCount(): MongoPipeline = {
    factPipeline(
      "IntegrityCheckCount",
      filter(
        and(
          equal("active", true),
          exists("country"),
        )
      ),
      unwind("$labels"),
      filter(
        and(
          equal("labels", BsonDocument.parse("""{"$regex": "^integrity-check-"}""")),
          not(
            equal("labels", BsonDocument.parse("""{"$regex": "^integrity-check-failed"}"""))
          )
        )
      ),
      project(
        fields(
          include("country"),
          computed("routeType", BsonDocument.parse("""{"$substr": ["$labels", 16, 99]}"""))
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$routeType"
          )
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineNodeIntegrityCheckFailedCount(): MongoPipeline = {
    factPipeline(
      "IntegrityCheckFailedCount",
      filter(
        and(
          equal("active", true),
          exists("country"),
        )
      ),
      unwind("$labels"),
      filter(
        equal("labels", BsonDocument.parse("""{"$regex": "^integrity-check-failed-"}"""))
      ),
      project(
        fields(
          include("country"),
          computed("routeType", BsonDocument.parse("""{"$substr": ["$labels", 23, 99]}"""))
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "country", "$country",
            "routeType", "$routeType"
          )
        ),
        sum("value", 1)
      )
    )
  }
}
