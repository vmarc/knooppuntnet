package kpn.database.actions.nodes

import kpn.api.common.common.Reference
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryNodeBaseRouteReferences.log
import kpn.database.base.Database
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryNodeBaseRouteReferences {
  private val log = Log(classOf[MongoQueryNodeBaseRouteReferences])
}

class MongoQueryNodeBaseRouteReferences(database: Database) {

  def execute(nodeId: Long): Seq[Reference] = {
    log.infoElapsed {
      val refs = database.baseRoutes.aggregate[Reference](pipeline(nodeId), log)
      (s"node route refs: ${refs.size}", refs)
    }
  }

  private def pipeline(nodeId: Long): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("nodeRefs", nodeId),
        )
      ),
      unwind("$summary.routeTypes"),
      unwind("$summary.scopes"),
      project(
        fields(
          excludeId(),
          computed("routeType", "$summary.routeTypes"),
          computed("routeScope", "$summary.scopes"),
          computed("id", "$summary.id"),
          computed("name", "$summary.name")
        )
      ),
      sort(orderBy(ascending("routeType", "routeScope", "routeName")))
    )
  }
}
