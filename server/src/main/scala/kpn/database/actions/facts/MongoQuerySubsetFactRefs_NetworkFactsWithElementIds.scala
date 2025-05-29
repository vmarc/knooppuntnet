package kpn.database.actions.facts

import kpn.api.common.Fact
import kpn.api.common.location.Ids
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_NetworkFactsWithElementIds.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

object MongoQuerySubsetFactRefs_NetworkFactsWithElementIds {
  private val log = Log(classOf[MongoQuerySubsetFactRefs_NetworkFactsWithElementIds])
}

class MongoQuerySubsetFactRefs_NetworkFactsWithElementIds(database: Database) {

  def execute(subset: Subset, fact: Fact, elementType: String): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, fact)
      val elementIds = database.networks.aggregate[Ids](pipeline, log).flatMap(_.ids)
      val subsetFactRefs = SubsetFactRefs(elementType, elementIds)
      (s"network element references '$elementType': ${elementIds.mkString(", ")}", subsetFactRefs)
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
          computed("ids", "$facts.elementIds"),
        )
      )
    )
  }
}
