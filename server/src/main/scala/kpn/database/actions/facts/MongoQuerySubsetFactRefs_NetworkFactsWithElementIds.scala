package kpn.database.actions.facts

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.Fact
import kpn.api.common.location.Ids
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_NetworkFactsWithElementIds.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubsetFactRefs_NetworkFactsWithElementIds {
  private val log = Log(classOf[MongoQuerySubsetFactRefs_NetworkFactsWithElementIds])
}

class MongoQuerySubsetFactRefs_NetworkFactsWithElementIds(database: Database) {

  def execute(subset: Subset, fact: Fact, elementType: String): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, fact)
      val elementIds = database.networks.aggregate(pipeline, classOf[Ids], log).flatMap(_.ids)
      val subsetFactRefs = SubsetFactRefs(elementType, elementIds)
      (s"network element references '$elementType': ${elementIds.mkString(", ")}", subsetFactRefs)
    }
  }

  private def buildPipeline(subset: Subset, fact: Fact): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.toString),
          equal("summary.routeType", subset.routeType.toString)
        )
      ),
      unwind("$facts"),
      filter(equal("facts.fact", fact.toString)),
      project(
        fields(
          excludeId(),
          computed("ids", "$facts.elementIds"),
        )
      )
    )
  }
}
