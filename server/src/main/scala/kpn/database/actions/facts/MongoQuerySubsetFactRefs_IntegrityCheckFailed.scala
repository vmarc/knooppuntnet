package kpn.database.actions.facts

import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_IntegrityCheckFailed.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQuerySubsetFactRefs_IntegrityCheckFailed {
  private val log = Log(classOf[MongoQuerySubsetFactRefs_IntegrityCheckFailed])
}

class MongoQuerySubsetFactRefs_IntegrityCheckFailed(database: Database) {
  def execute(subset: Subset): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset)
      val refs = database.nodes.aggregate[Id](pipeline, log).map(_._id)
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
