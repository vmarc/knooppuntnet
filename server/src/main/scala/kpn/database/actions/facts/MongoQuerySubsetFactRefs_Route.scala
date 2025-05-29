package kpn.database.actions.facts

import kpn.api.common.Fact
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQuerySubsetFactRefs_Route.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQuerySubsetFactRefs_Route {
  private val log = Log(classOf[MongoQuerySubsetFactRefs_Route])
}

class MongoQuerySubsetFactRefs_Route(database: Database) {

  def execute(subset: Subset, fact: Fact): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, fact)
      val refs = database.routes.aggregate[Id](pipeline, log).map(_._id)
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
