
package kpn.database.actions.facts

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.Fact
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_NetworkFactsWithElementRefs.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubsetFactRefs_NetworkFactsWithElementRefs {
  private val log = Log(classOf[MongoQuerySubsetFactRefs_NetworkFactsWithElementRefs])
}

class MongoQuerySubsetFactRefs_NetworkFactsWithElementRefs(database: Database) {

  def execute(subset: Subset, fact: Fact, elementType: String): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, fact)
      val elementIds = database.networks.aggregate(pipeline, classOf[Id], log).map(_._id)
      val subsetFactRefs = SubsetFactRefs(elementType, elementIds)
      (s"network element ref references '$elementType': ${elementIds.mkString(", ")}", subsetFactRefs)
    }
  }

  private def buildPipeline(subset: Subset, fact: Fact): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.entryName),
          equal("summary.routeType", subset.routeType.entryName)
        )
      ),
      unwind("$facts"),
      filter(equal("facts.fact", fact.entryName)),
      unwind("$facts.elements"),
      project(
        fields(
          excludeId(),
          computed("_id", "$facts.elements.id"),
        )
      )
    )
  }
}
