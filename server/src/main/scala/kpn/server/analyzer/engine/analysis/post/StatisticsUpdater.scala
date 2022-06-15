package kpn.server.analyzer.engine.analysis.post

import kpn.api.common.statistics.StatisticValue
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoProjections.concat
import kpn.database.util.Mongo
import org.mongodb.scala.Document
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.out
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unionWith
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Filters.not
import org.mongodb.scala.model.Filters.notEqual
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy
import org.springframework.stereotype.Component

object StatisticsUpdater {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new StatisticsUpdater(database).execute()
    }
  }
}

@Component
class StatisticsUpdater(database: Database) {

  private val log = Log(classOf[StatisticsUpdater])

  def execute(): Unit = {

    log.debugElapsed {
      val pipeline = Seq(
        pipelineNodeCount(),
        Seq(unionWith(database.orphanNodes.name, pipelineOrphanNodeCount(): _*)),
        Seq(unionWith(database.routes.name, pipelineRouteCount(): _*)),
        Seq(unionWith(database.orphanRoutes.name, pipelineOrphanRouteCount(): _*)),
        Seq(unionWith(database.nodes.name, pipelineNodeFacts(): _*)),
        Seq(unionWith(database.nodes.name, pipelineNodeIntegrityCheckCount(): _*)),
        Seq(unionWith(database.nodes.name, pipelineNodeIntegrityCheckFailedCount(): _*)),
        Seq(unionWith(database.routes.name, pipelineRouteFacts(): _*)),
        Seq(unionWith(database.routes.name, pipelineRouteDistance(): _*)),
        Seq(unionWith(database.networkInfos.name, pipelineNetworkCount(): _*)),
        Seq(unionWith(database.networkInfos.name, pipelineNetworkFacts(): _*)),
        Seq(unionWith(database.networkInfos.name, pipelineNetworkFacts2(): _*)),
        Seq(unionWith(database.networkInfos.name, pipelineNetworkFacts3(): _*)),
        Seq(unionWith(database.networkInfos.name, factCountPipeline(): _*)),
        Seq(unionWith(database.networkInfos.name, pipelineIntegrityCheckNetworkCount(): _*)),
        Seq(unionWith(database.changes.name, pipelineChangeCount(): _*)),
        Seq(out(database.statistics.name))
      ).flatten

      val values = database.nodes.aggregate[StatisticValue](pipeline)
      (s"${values.size} values", ())
    }
  }

  private def pipelineNodeCount(): Seq[Bson] = {
    factPipeline(
      "NodeCount",
      filter(
        and(
          equal("labels", Label.active),
          exists("country")
        )
      ),
      unwind("$names"),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$names.networkType"
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineOrphanNodeCount(): Seq[Bson] = {
    factPipeline(
      "OrphanNodeCount",
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$networkType"
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineRouteCount(): Seq[Bson] = {
    factPipeline(
      "RouteCount",
      filter(
        and(
          equal("labels", Label.active),
          exists("summary.country")
        )
      ),
      group(
        Document(
          "country" -> "$summary.country",
          "networkType" -> "$summary.networkType"
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineOrphanRouteCount(): Seq[Bson] = {
    factPipeline(
      "OrphanRouteCount",
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$networkType"
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineNodeFacts(): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("labels", Label.active),
          exists("country"),
          exists("facts")
        )
      ),
      unwind("$names"),
      unwind("$facts"),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$names.networkType",
          "factName" -> "$facts"
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
              computed("networkType", "$_id.networkType"),
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

  private def pipelineRouteFacts(): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("labels", Label.active),
          exists("summary.country"),
          exists("facts")
        )
      ),
      unwind("$facts"),
      group(
        Document(
          "country" -> "$summary.country",
          "networkType" -> "$summary.networkType",
          "factName" -> "$facts"
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
              computed("networkType", "$_id.networkType"),
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

  private def pipelineNetworkFacts(): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.networkType"),
          exists("facts")
        )
      ),
      unwind("$facts"),
      filter(
        in(
          "facts.name",
          "NetworkExtraMemberNode",
          "NetworkExtraMemberWay",
          "NetworkExtraMemberRelation",
        )
      ),
      project(
        fields(
          computed("factName", "$facts.name"),
          include("country"),
          computed("networkType", "$summary.networkType"),
          BsonDocument("""{"factCount": { "$size": "$facts.elementIds" }}""")
        )
      ),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$networkType",
          "factName" -> "$factName"
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
              computed("networkType", "$_id.networkType"),
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

  private def pipelineNetworkFacts2(): Seq[Bson] = {

    Seq(
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.networkType"),
          exists("facts")
        )
      ),
      unwind("$facts"),
      filter(
        in(
          "facts.name",
          "NodeMemberMissing",
        )
      ),
      project(
        fields(
          computed("factName", "$facts.name"),
          include("country"),
          computed("networkType", "$summary.networkType"),
          BsonDocument("""{"factCount": { "$size": "$facts.elements" }}""")
        )
      ),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$networkType",
          "factName" -> "$factName"
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
              computed("networkType", "$_id.networkType"),
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

  private def pipelineNetworkFacts3(): Seq[Bson] = {

    Seq(
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.networkType"),
          exists("facts")
        )
      ),
      unwind("$facts"),
      filter(
        in(
          "facts.name",
          "NameMissing",
        )
      ),
      project(
        fields(
          computed("factName", "$facts.name"),
          include("country"),
          computed("networkType", "$summary.networkType"),
          computed("factCount", 1)
        )
      ),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$networkType",
          "factName" -> "$factName"
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
              computed("networkType", "$_id.networkType"),
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

  private def pipelineRouteDistance(): Seq[Bson] = {
    factPipeline(
      "Distance",
      filter(
        and(
          equal("labels", Label.active),
          exists("summary.country")
        )
      ),
      group(
        Document(
          "country" -> "$summary.country",
          "networkType" -> "$summary.networkType"
        ),
        sum("value", "$summary.meters")
      ),
      project(
        fields(
          include("_id"),
          computed("value", BsonDocument("""{$divide: ["$value", 1000]}"""))
        )
      )
    )
  }

  private def pipelineNetworkCount(): Seq[Bson] = {
    factPipeline(
      "NetworkCount",
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.networkType")
        )
      ),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$summary.networkType"
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineIntegrityCheckNetworkCount(): Seq[Bson] = {
    factPipeline(
      "IntegrityCheckNetworkCount",
      filter(
        and(
          equal("active", true),
          exists("country"),
          exists("summary.networkType"),
          equal("detail.integrity.hasChecks", true),
        )
      ),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$summary.networkType"
        ),
        sum("value", 1)
      )
    )
  }

  private def factCountPipeline(): Seq[Bson] = {
    Seq(
      networkFactCountPipeline(),
      Seq(unionWith(database.nodes.name, nodeFactCountPipeline(): _*)),
      Seq(unionWith(database.routes.name, routeFactCountPipeline(): _*)),
      combineFactCounts()
    ).flatten
  }

  private def combineFactCounts(): Seq[Bson] = {
    Seq(
      group(
        "$_id",
        sum("factCount", "$factCount")
      ),
      project(
        fields(
          computed("country", "$_id.country"),
          computed("networkType", "$_id.networkType"),
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
              computed("networkType", "$_id.networkType"),
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

  private def networkFactCountPipeline(): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("active", true),
          exists("country")
        )
      ),
      unwind("$facts"),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$summary.networkType"
        ),
        sum("factCount", 1)
      )
    )
  }

  private def routeFactCountPipeline(): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("labels", Label.active),
          exists("summary.country")
        )
      ),
      unwind("$facts"),
      filter(
        and(
          notEqual("facts", "RouteBroken"),
          notEqual("facts", "RouteNotForward"),
          notEqual("facts", "RouteNotBackward"),
        )
      ),
      group(
        Document(
          "country" -> "$summary.country",
          "networkType" -> "$summary.networkType"
        ),
        sum("factCount", 1)
      )
    )
  }

  private def nodeFactCountPipeline(): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("labels", Label.active)
        )
      ),
      unwind("$names"),
      unwind("$facts"),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$names.networkType"
        ),
        sum("factCount", 1)
      )
    )
  }

  private def pipelineChangeCount(): Seq[Bson] = {
    factPipeline(
      "ChangeCount",
      unwind("$subsets"),
      group(
        Document(
          "country" -> "$subsets.country",
          "networkType" -> "$subsets.networkType"
        ),
        sum("value", 1)
      )
    )
  }

  private def factPipeline(name: String, aggregateElements: Bson*): Seq[Bson] = {
    aggregateElements ++
      Seq(
        sort(orderBy(ascending("_id"))),
        project(
          fields(
            computed(
              "values",
              fields(
                computed("country", "$_id.country"),
                computed("networkType", "$_id.networkType"),
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

  private def pipelineNodeIntegrityCheckCount(): Seq[Bson] = {
    factPipeline(
      "IntegrityCheckCount",
      filter(
        and(
          equal("labels", Label.active),
          exists("country"),
        )
      ),
      unwind("$labels"),
      filter(
        and(
          equal("labels", BsonDocument("""{"$regex": "^integrity-check-"}""")),
          not(
            equal("labels", BsonDocument("""{"$regex": "^integrity-check-failed"}"""))
          )
        )
      ),
      project(
        fields(
          include("country"),
          computed("networkType", BsonDocument("""{"$substr": ["$labels", 16, 99]}"""))
        )
      ),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$networkType"
        ),
        sum("value", 1)
      )
    )
  }

  private def pipelineNodeIntegrityCheckFailedCount(): Seq[Bson] = {
    factPipeline(
      "IntegrityCheckFailedCount",
      filter(
        and(
          equal("labels", Label.active),
          exists("country"),
        )
      ),
      unwind("$labels"),
      filter(
        equal("labels", BsonDocument("""{"$regex": "^integrity-check-failed-"}"""))
      ),
      project(
        fields(
          include("country"),
          computed("networkType", BsonDocument("""{"$substr": ["$labels", 23, 99]}"""))
        )
      ),
      group(
        Document(
          "country" -> "$country",
          "networkType" -> "$networkType"
        ),
        sum("value", 1)
      )
    )
  }
}
