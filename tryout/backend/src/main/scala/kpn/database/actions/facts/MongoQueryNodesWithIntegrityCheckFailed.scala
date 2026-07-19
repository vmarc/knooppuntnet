package kpn.database.actions.facts

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.common.Ref
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQueryNodesWithIntegrityCheckFailed.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryNodesWithIntegrityCheckFailed {
  private val log = Log(classOf[MongoQueryNodesWithIntegrityCheckFailed])
}

class MongoQueryNodesWithIntegrityCheckFailed(database: Database) {

  def execute(subset: Subset): Seq[Ref] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset)
      val refs = database.nodes.aggregate(pipeline, classOf[Ref], log)
      (s"nodeRefs: ${refs.size}", refs)
    }
  }

  private def buildPipeline(subset: Subset): MongoPipeline = {
    val factLabel = s"integrity-check-failed-${subset.routeType.entryName}"
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.country(subset.country)),
          equal("labels", Label.routeType(subset.routeType)),
          equal("labels", factLabel),
        )
      ),
      unwind("$names"),
      filter(equal("base.names.routeType", subset.routeType.entryName)),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$base.names.name"),
        )
      )
    )
  }
}
