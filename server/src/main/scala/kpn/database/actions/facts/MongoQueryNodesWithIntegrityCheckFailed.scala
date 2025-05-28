package kpn.database.actions.facts

import kpn.api.common.common.Ref
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQueryNodesWithIntegrityCheckFailed.log
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

object MongoQueryNodesWithIntegrityCheckFailed {
  private val log = Log(classOf[MongoQueryNodesWithIntegrityCheckFailed])
}

class MongoQueryNodesWithIntegrityCheckFailed(database: Database) {

  def execute(subset: Subset): Seq[Ref] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset)
      val refs = database.nodes.aggregate[Ref](pipeline, log)
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
      filter(equal("names.routeType", subset.routeType.entryName)),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$names.name"),
        )
      )
    )
  }
}
