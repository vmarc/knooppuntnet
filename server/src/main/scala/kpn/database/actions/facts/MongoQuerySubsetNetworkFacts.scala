package kpn.database.actions.facts

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.Fact
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetNetworkFacts.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubsetNetworkFacts {
  private val log = Log(classOf[MongoQuerySubsetNetworkFacts])
}

class MongoQuerySubsetNetworkFacts(database: Database) {

  def execute(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, fact)
      val references = database.networks.aggregate(pipeline, classOf[NetworkFactRefs], log)
      (s"network fact references: ${references.size}", references)
    }
  }

  private def buildPipeline(subset: Subset, fact: Fact): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.entryName),
          equal("base.routeType", subset.routeType.entryName)
        )
      ),
      unwind("$facts"),
      filter(equal("facts.fact", fact.entryName)),
      project(
        fields(
          excludeId(),
          computed("networkId", "$_id"),
          computed("networkName", "$base.name"),
          computed("factRefs", "$facts.elements"),
        )
      )
    )
  }
}
