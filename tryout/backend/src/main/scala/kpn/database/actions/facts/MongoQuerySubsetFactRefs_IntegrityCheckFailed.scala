package kpn.database.actions.facts

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_IntegrityCheckFailed.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubsetFactRefs_IntegrityCheckFailed {
  private val log = Log(classOf[MongoQuerySubsetFactRefs_IntegrityCheckFailed])
}

class MongoQuerySubsetFactRefs_IntegrityCheckFailed(database: Database) {
  def execute(subset: Subset): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset)
      val refs = database.nodes.aggregate(pipeline, classOf[Id], log).map(_._id)
      (s"nodeRefs: ${refs.size}", SubsetFactRefs("node", refs))
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
      project(
        fields(
          include("_id")
        )
      )
    )
  }
}
