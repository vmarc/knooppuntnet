package kpn.database.actions.facts

import kpn.api.common.Fact
import kpn.api.common.common.Ref
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetNetworkElementIdFacts.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.repository.NetworkFactElementIds
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

object MongoQuerySubsetNetworkElementIdFacts {
  private val log = Log(classOf[MongoQuerySubsetNetworkElementIdFacts])
}

class MongoQuerySubsetNetworkElementIdFacts(database: Database) {

  def execute(subset: Subset, fact: Fact): Seq[NetworkFactRefs] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, fact)
      val elementReferences = database.networks.aggregate[NetworkFactElementIds](pipeline, log)
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
          equal("country", subset.country.entryName),
          equal("summary.routeType", subset.routeType.entryName)
        )
      ),
      unwind("$facts"),
      filter(equal("facts.fact", fact.entryName)),
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
