package kpn.database.actions.facts

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.Fact
import kpn.api.common.common.Ref
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetNetworkElementIdFacts.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.repository.NetworkFactElementIds

object MongoQuerySubsetNetworkElementIdFacts {
  private val log = Log(classOf[MongoQuerySubsetNetworkElementIdFacts])
}

class MongoQuerySubsetNetworkElementIdFacts(database: Database) {

  def execute(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, fact)
      val elementReferences = database.networks.aggregate(pipeline, classOf[NetworkFactElementIds], log)
      val references = elementReferences.map { reference =>
        NetworkFactRefs(
          reference.networkId,
          reference.networkName,
          reference.elementIds.map(id => Ref(id, id.toString))
        )
      }
      (s"network element references: ${references.size}", references)
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
          computed("networkId", "$_id"),
          computed("networkName", "$summary.name"),
          computed("elementIds", "$facts.elementIds"),
        )
      )
    )
  }
}
