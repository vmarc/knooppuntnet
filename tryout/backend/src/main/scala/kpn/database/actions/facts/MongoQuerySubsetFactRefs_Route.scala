package kpn.database.actions.facts

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.Fact
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_Route.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubsetFactRefs_Route {
  private val log = Log(classOf[MongoQuerySubsetFactRefs_Route])
}

class MongoQuerySubsetFactRefs_Route(database: Database) {

  def execute(subset: Subset, fact: Fact): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, fact)
      val refs = database.routes.aggregate(pipeline, classOf[Id], log).map(_._id)
      (s"routeRefs: ${refs.size}", SubsetFactRefs("relation", refs))
    }
  }

  private def buildPipeline(subset: Subset, fact: Fact): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.country(subset.country)),
          equal("labels", Label.routeType(subset.routeType)),
          equal("labels", Label.fact(fact)),
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
  }
}
