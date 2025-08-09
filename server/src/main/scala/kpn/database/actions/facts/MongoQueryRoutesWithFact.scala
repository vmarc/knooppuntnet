package kpn.database.actions.facts

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.Fact
import kpn.api.common.common.Ref
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQueryRoutesWithFact.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryRoutesWithFact {
  private val log = Log(classOf[MongoQueryRoutesWithFact])
}

class MongoQueryRoutesWithFact(database: Database) {
  def execute(subset: Subset, fact: Fact): Seq[Ref] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, fact)
      val refs = database.routes.aggregate(pipeline, classOf[Ref], log)
      (s"routeRefs: ${refs.size}", refs)
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
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$summary.name"),
        )
      )
    )
  }
}
